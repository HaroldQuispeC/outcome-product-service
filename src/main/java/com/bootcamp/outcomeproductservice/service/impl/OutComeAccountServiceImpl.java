package com.bootcamp.outcomeproductservice.service.impl;

import com.bootcamp.outcomeproductservice.entity.BankAccount;
import com.bootcamp.outcomeproductservice.entity.BankAccountOwner;
import com.bootcamp.outcomeproductservice.entity.OutComeAccount;
import com.bootcamp.outcomeproductservice.model.Client;
import com.bootcamp.outcomeproductservice.repository.OutComeAccountRepository;
import com.bootcamp.outcomeproductservice.service.ClientService;
import com.bootcamp.outcomeproductservice.service.OutComeAccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

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
    public ResponseEntity<Mono<OutComeAccount>> createOutComeAccountByDni(OutComeAccount outComeAccount) {
        logger.info("PROCESS TO CREATE OUTCOMEACCOUNTBYDNI");
        int sizeIdentifier = outComeAccount.getIdentifier().length();

        if(sizeIdentifier == 8) {
            logger.info("FIND CLIENT TO MS-CLIENT");
            Client clientNaturalPerson = feingClientService.findByDocumenNumber(outComeAccount.getIdentifier());

            if(!clientNaturalPerson.equals("")){
                outComeAccount.setClient(clientNaturalPerson);
                Mono<OutComeAccount> saveAccount= outComeAccountRepository.save(outComeAccount);
                return new ResponseEntity<>(saveAccount, HttpStatus.OK);
            }
            logger.info("ACCOUNT CREATED SUCCESS");
        } else {
            logger.info("ERROR IN REQUEST");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return null;
    }

    @Override
    public ResponseEntity<Mono<OutComeAccount>> createOutComeAccountByRuc(OutComeAccount outComeAccount) {
        logger.info("PROCESS TO CREATE OUTCOMEACCOUNTBYRUC");

        int sizeIdentifier = outComeAccount.getIdentifier().length();

        if(sizeIdentifier == 11) {
            logger.info("Find BusinessClien into ms-client");
            Client clientBusiness = feingClientService.findByRuc(outComeAccount.getIdentifier());
            if(!clientBusiness.equals("")){
                outComeAccount.setClient(clientBusiness);
                String dni = outComeAccount.getIdentifier().substring(2,10);
                Client retrieveClient = feingClientService.findByDocumenNumber(dni);
                ArrayList<BankAccountOwner> owners = new ArrayList<>();
                BankAccountOwner bankAccountOwner = new BankAccountOwner();
                bankAccountOwner.setStatus("ACTIVE");
                bankAccountOwner.setClient(retrieveClient);
                owners.add(bankAccountOwner);

                Mono<OutComeAccount> saveAccount = outComeAccountRepository.save(outComeAccount);
                outComeAccount.getBankAccounts().get(0).setBankAccountOwners(owners);
                return new ResponseEntity<>(saveAccount, HttpStatus.OK);

            }
        } else {
            logger.info("Error in request");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return null;
    }

    @Override
    public ResponseEntity<Mono<List<BankAccount>>> findAccountsByDni(String dni) {
        Mono<OutComeAccount> outComeAccount =  outComeAccountRepository.findAll()
                .filter(outCome -> outCome.getIdentifier().equals(dni)).take(1).single()
                .onErrorResume(
                        error -> {
                            logger.error("No se encuentra registrado el DNI" + dni, error.getMessage());
                            return Mono.empty();
                        });

        ArrayList<BankAccount> accounts = outComeAccount.block().getBankAccounts();

        Mono<List<BankAccount>> getAccounts = Mono.just(accounts);
        return new ResponseEntity<>(getAccounts, HttpStatus.OK);
    }
}
