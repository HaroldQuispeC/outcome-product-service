package com.bootcamp.outcomeproductservice.controller;

import com.bootcamp.outcomeproductservice.entity.BankAccount;
import com.bootcamp.outcomeproductservice.entity.OutComeAccount;
import com.bootcamp.outcomeproductservice.service.OutComeAccountService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@RestController
@RequestMapping("/api/outcomes")
public class OutComeAccountController {

  private static final Logger logger = LoggerFactory.getLogger(OutComeAccountController.class);

  @Autowired
  private OutComeAccountService outComeAccountService;

  @GetMapping("/")
  public Flux<OutComeAccount> findAllOutCome() {
    return outComeAccountService.findAll();
  }

  @GetMapping("/findByAccountSerialNumber")
  public Mono<BankAccount> findAccountByAccountSerialNumber(
          @RequestParam(value = "identifier", required = true) String identifier,
          @RequestParam(value = "accountSerialNumber", required = true)
                  String accountSerialNumber) {
    return outComeAccountService.findByAccountSerialNumber(identifier, accountSerialNumber);
  }

  @CircuitBreaker(name = "clientCB", fallbackMethod = "fallBackSaveOutComeAccountByDni")
  @PostMapping("/createOutComeAccountByDni")
  public ResponseEntity<Mono<OutComeAccount>> createOutComeAccountByDni(
          @RequestBody OutComeAccount outComeAccount) {
    return outComeAccountService.createOutComeAccountByDni(outComeAccount);
  }

  @CircuitBreaker(name = "clientCB", fallbackMethod = "fallBackSaveOutComeAccountByRuc")
  @PostMapping("/createOutComeAccountByRuc")
  public ResponseEntity<Mono<OutComeAccount>> createOutComeAccountByRuc(
          @RequestBody OutComeAccount outComeAccount) {
    return outComeAccountService.createOutComeAccountByRuc(outComeAccount);
  }

  @PostMapping("/addAccountByRuc")
  public ResponseEntity<Mono<OutComeAccount>> addAccountByRuc(
          @RequestBody OutComeAccount outComeAccount) {
    return outComeAccountService.addBankAccountByRuc(outComeAccount);
  }

  //@CircuitBreaker(name = "clientCB", fallbackMethod = "fallBackGetAccounts")
  @GetMapping("/findAccountByDni/{dni}")
  public Mono<List<BankAccount>> getAccountsByDdni(@PathVariable("dni") String dni) {
    return outComeAccountService.findAccountsByDni(dni);
  }

  @GetMapping("/findAccountByRuc/{ruc}")
  public Flux<BankAccount> getAccountsByRuc(@PathVariable("ruc") String ruc) {
    return outComeAccountService.findAccountsByRuc(ruc);
  }

  @PostMapping("/addAccountOwner/{dni}/{ruc}/{accountSerialNumber}")
  public ResponseEntity<Mono<OutComeAccount>> addAccountOwner(
          @PathVariable("dni") String dni, @PathVariable("ruc") String ruc,
          @PathVariable("accountSerialNumber") String accountSerialNumber) {
    return outComeAccountService.addAccountOwner(dni, ruc, accountSerialNumber);
  }

  @PostMapping("/addAccountSigner/{dni}/{ruc}/{accountSerialNumber}")
  public ResponseEntity<Mono<OutComeAccount>> addAccountSigner(
          @PathVariable("dni") String dni, @PathVariable("ruc") String ruc,
          @PathVariable("accountSerialNumber") String accountSerialNumber) {
    return outComeAccountService.addAccountSigner(dni, ruc, accountSerialNumber);
  }


  //------------------Implementation of Circuit Breaker
  private ResponseEntity<Mono<OutComeAccount>> fallBackSaveOutComeAccountByDni(
          @RequestBody OutComeAccount outComeAccount, RuntimeException e) {
    logger.info("El DNI ".concat(outComeAccount.getIdentifier())
            .concat(" no está creado en la BD"));
    OutComeAccount outcomeAccount = new OutComeAccount();
    Mono<OutComeAccount> response = Mono.just(outcomeAccount);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  private ResponseEntity<Mono<OutComeAccount>> fallBackSaveOutComeAccountByRuc(
          @RequestBody OutComeAccount outComeAccount, RuntimeException e) {
    logger.info("El RUC ".concat(outComeAccount.getIdentifier())
            .concat(" aún no está registrado en SUNARP"));
    OutComeAccount outcomeAccount = new OutComeAccount();
    Mono<OutComeAccount> response = Mono.just(outcomeAccount);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  private ResponseEntity<Mono<List<BankAccount>>> fallBackGetAccounts(
          @PathVariable("dni") String dni, RuntimeException e) {
    logger.info("El DNI ".concat(dni).concat(" se encuentra en la lista negra."));
    List<BankAccount> list = new ArrayList<>();
    Mono<List<BankAccount>> response = Mono.just(list);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }
}
