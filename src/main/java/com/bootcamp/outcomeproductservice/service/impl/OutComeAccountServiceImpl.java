package com.bootcamp.outcomeproductservice.service.impl;

import com.bootcamp.outcomeproductservice.entity.BankAccount;
import com.bootcamp.outcomeproductservice.entity.BankAccountOwner;
import com.bootcamp.outcomeproductservice.entity.BankAccountSigner;
import com.bootcamp.outcomeproductservice.entity.OutComeAccount;
import com.bootcamp.outcomeproductservice.exceptions.ModelException;
import com.bootcamp.outcomeproductservice.model.Client;
import com.bootcamp.outcomeproductservice.repository.OutComeAccountRepository;
import com.bootcamp.outcomeproductservice.service.ClientService;
import com.bootcamp.outcomeproductservice.service.OutComeAccountService;
import com.bootcamp.outcomeproductservice.utils.Constants;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class OutComeAccountServiceImpl implements OutComeAccountService {
  private static final Logger logger = LoggerFactory.getLogger(OutComeAccountServiceImpl.class);

  @Autowired
  private OutComeAccountRepository outComeAccountRepository;

  @Autowired
  private ClientService feingClientService;

  @Override
  public Flux<OutComeAccount> findAll() {
    return outComeAccountRepository.findAll();
  }

  @Override
  public ResponseEntity<Mono<OutComeAccount>> createOutComeAccountByDni(
          OutComeAccount outComeAccount) {

    if (outComeAccount == null) {
      throw new ModelException("OutcomeAccount object null or invalid");
    }

    if (outComeAccount.getIdentifier() == null) {
      throw new ModelException("Identifier is null or invalid");
    }

    logger.info("PROCESS TO CREATE OUTCOMEACCOUNTBYDNI");
    int sizeIdentifier = outComeAccount.getIdentifier().length();

    if (sizeIdentifier == 8) {
      logger.info("FIND CLIENT TO MS-CLIENT");
      Client clientNaturalPerson = feingClientService.findByDocumenNumber(
              outComeAccount.getIdentifier());
      if (!clientNaturalPerson.equals("")) {
        String accountSerialNumber = Constants.INITIAL_ACCOUNT_SERIAL_NUMBER_OF_CLIENT
                .concat(generateAccountNumber());
        outComeAccount.setClient(clientNaturalPerson);
        outComeAccount.getBankAccounts().get(0).setAccountSerialNumber(accountSerialNumber);
        outComeAccount.getBankAccounts().get(0).getDebitCard()
                .setCreditCardSn(Constants.INITIAL_DEBIT_CARD_SERIAL_NUMBER
                        .concat(generateDebitCardNumber()));
        outComeAccount.getBankAccounts().get(0).getDebitCard()
                .setExpirationDate(expirationDateOfDebitCard());
        outComeAccount.getBankAccounts().get(0).getDebitCard().setCvv(generateCvvDebitCard());

        Mono<OutComeAccount> saveAccount = outComeAccountRepository.save(outComeAccount);
        return new ResponseEntity<>(saveAccount, HttpStatus.OK);
      }
      logger.info("ACCOUNT CREATED SUCCESS");
    } else {
      logger.info("ERROR IN REQUEST");
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @Override
  public ResponseEntity<Mono<OutComeAccount>> createOutComeAccountByRuc(
          OutComeAccount outComeAccount) {
    logger.info("PROCESS TO CREATE OUTCOMEACCOUNTBYRUC");

    int sizeIdentifier = outComeAccount.getIdentifier().length();

    if (sizeIdentifier == 11) {
      logger.info("Find BusinessClien into ms-client");
      Client clientBusiness = feingClientService.findByRuc(outComeAccount.getIdentifier());
      if (!clientBusiness.equals("")) {
        outComeAccount.setClient(clientBusiness);

        String accountSerialNumber = Constants.INITIAL_ACCOUNT_SERIAL_NUMBER_OF_BUSINESS
                .concat(generateAccountNumber());

        outComeAccount.getBankAccounts().get(0).setAccountSerialNumber(accountSerialNumber);

        outComeAccount.getBankAccounts().get(0).getDebitCard()
                .setCreditCardSn(Constants.INITIAL_DEBIT_CARD_SERIAL_NUMBER
                        .concat(generateDebitCardNumber()));
        outComeAccount.getBankAccounts().get(0).getDebitCard()
                .setExpirationDate(expirationDateOfDebitCard());
        outComeAccount.getBankAccounts().get(0).getDebitCard()
                .setCvv(generateCvvDebitCard());

        String dni = outComeAccount.getIdentifier().substring(2, 10);
        Client retrieveClient = feingClientService.findByDocumenNumber(dni);

        ArrayList<BankAccountOwner> owners = addBankAccountOwner(retrieveClient);
        outComeAccount.getBankAccounts().get(0).setBankAccountOwners(owners);
        Mono<OutComeAccount> saveAccount = outComeAccountRepository.save(outComeAccount);
        return new ResponseEntity<>(saveAccount, HttpStatus.OK);
      }
      logger.info("ACCOUNT CREATED SUCCESS");
    } else {
      logger.info("Error in request");
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @Override
  public Mono<List<BankAccount>> findAccountsByDni(String dni) {
    Mono<OutComeAccount> outComeAccountbyDni = outComeAccountRepository.findAll()
            .filter(outCome -> outCome.getIdentifier().equals(dni)).take(1).single()
            .onErrorResume(error -> {
              logger.error("No se encuentra registrado el DNI" + dni, error.getMessage());
              return Mono.empty();
            });

    ArrayList<BankAccount> accounts = outComeAccountbyDni.block().getBankAccounts();

    Mono<List<BankAccount>> getAccounts = Mono.just(accounts);
    return getAccounts;
  }

  @Override
  public Flux<BankAccount> findAccountsByRuc(String ruc) {
    Mono<OutComeAccount> outComeAccounts = outComeAccountRepository.findAll()
            .filter(outCome -> outCome.getIdentifier()
                    .equals(ruc)).take(1).single();

    ArrayList<BankAccount> accounts = outComeAccounts.block().getBankAccounts();
    Mono<List<BankAccount>> getAccounts = Mono.just(accounts);

    Flux<BankAccount> fba = getAccounts.flatMapIterable(list -> list);
    return fba;
  }

  @Override
  public ResponseEntity<Mono<OutComeAccount>> addAccountOwner(
          String dni, String ruc, String accountSerialNumber) {

    Mono<BankAccount> bankAccountMono = findAccountsByRuc(ruc)
            .filter(b -> b.getAccountSerialNumber().equals(accountSerialNumber)).take(1).single()
            .onErrorResume(error -> {
              logger.error("No se encuentra registrado el RUC" + ruc, error.getMessage());
              return Mono.empty();
            });

    Client retrieveClient = feingClientService.findByDocumenNumber(dni);
    BankAccountOwner bao = createBankAccountOwner(retrieveClient);
    ArrayList<BankAccountOwner> baOwner = bankAccountMono.block().getBankAccountOwners();
    baOwner.add(bao);

    Optional<BankAccount> optBankAccount = Optional.ofNullable(bankAccountMono.block());
    ArrayList<BankAccount> updateBankAccount = new ArrayList<>();

    if(optBankAccount.isPresent()){
      optBankAccount.get().setBankAccountOwners(baOwner);
      updateBankAccount.add(optBankAccount.get());
    }

    OutComeAccount nOutComeAccount = saveUpdateOutcome(updateBankAccount,ruc);

    return new ResponseEntity<>(outComeAccountRepository.save(nOutComeAccount), HttpStatus.OK);
  }

  @Override
  public ResponseEntity<Mono<OutComeAccount>> addAccountSigner(
          String dni, String ruc, String accountSerialNumber) {

    Mono<BankAccount> bankAccountMono = findAccountsByRuc(ruc)
            .filter(b -> b.getAccountSerialNumber().equals(accountSerialNumber)).take(1).single()
            .onErrorResume(error -> {
              logger.error("No se encuentra registrado el RUC" + ruc, error.getMessage());
              return Mono.empty();
            });

    Client retrieveClient = feingClientService.findByDocumenNumber(dni);
    BankAccountSigner bas = createBankAccountSigner(retrieveClient);
    ArrayList<BankAccountSigner> baSigner = bankAccountMono.block().getBankAccountSigners();
    baSigner.add(bas);

    Optional<BankAccount> optBankAccount = Optional.ofNullable(bankAccountMono.block());
    ArrayList<BankAccount> updateBankAccount = new ArrayList<>();

    if(optBankAccount.isPresent()){
      optBankAccount.get().setBankAccountSigners(baSigner);
      updateBankAccount.add(optBankAccount.get());
    }
    OutComeAccount oca = saveUpdateOutcome(updateBankAccount, ruc);

    return new ResponseEntity<>(outComeAccountRepository.save(oca),HttpStatus.OK);
  }

  public OutComeAccount saveUpdateOutcome(ArrayList<BankAccount> bankAccounts, String ruc) {
    OutComeAccount newOutComeAccount = new OutComeAccount();

    Mono<OutComeAccount> outCome = getOutComeAccount(ruc);

    Optional<OutComeAccount> optComeAccount = Optional.ofNullable(outCome.block());

    if (optComeAccount.isPresent()) {
      optComeAccount.get().setBankAccounts(bankAccounts);
      newOutComeAccount = optComeAccount.get();
      System.out.println("OBJETO BANKACCOUNT" + newOutComeAccount);
    }
    return newOutComeAccount;
  }

  public Mono<OutComeAccount> getOutComeAccount(String ruc) {
    Mono<OutComeAccount> accountMono = outComeAccountRepository.findAll()
            .filter(account -> account.getIdentifier().equals(ruc)).take(1).single();

    return accountMono;
  }

  public ArrayList<BankAccountOwner> addBankAccountOwner(Client client) {

    ArrayList<BankAccountOwner> owners = new ArrayList<>();
    BankAccountOwner bankAccountOwner = new BankAccountOwner();
    bankAccountOwner.setStatus("ACTIVE");
    bankAccountOwner.setClient(client);
    owners.add(bankAccountOwner);

    return owners;
  }

  public BankAccountOwner createBankAccountOwner(Client client) {
    BankAccountOwner bao = new BankAccountOwner();
    bao.setStatus("ACTIVE");
    bao.setClient(client);
    return bao;
  }

  public BankAccountSigner createBankAccountSigner(Client client) {
    BankAccountSigner bas = new BankAccountSigner();
    bas.setStatus("ACTIVE");
    bas.setClient(client);
    return bas;
  }

  public String generateAccountNumber() {
    int randomNumber = ThreadLocalRandom.current().nextInt(0, 999999999);
    String number = String.valueOf(randomNumber);
    return number;
  }

  public String generateDebitCardNumber() {
    int randomNumber = ThreadLocalRandom.current().nextInt(0, 99999999);
    String response = String.valueOf(randomNumber);
    return response;
  }

  public String expirationDateOfDebitCard() {
    LocalDate dt = LocalDate.now().plusYears(5);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/YY");
    String response = formatter.format(dt).toString();

    return response;
  }

  public String generateCvvDebitCard() {
    int randomNumber = ThreadLocalRandom.current().nextInt(100, 999);
    String response = String.valueOf(randomNumber);
    return response;
  }
}
