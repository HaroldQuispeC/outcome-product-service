package com.bootcamp.outcomeproductservice.controller;

import com.bootcamp.outcomeproductservice.entity.BankAccount;
import com.bootcamp.outcomeproductservice.entity.OutComeAccount;
import com.bootcamp.outcomeproductservice.service.OutComeAccountService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/outcomes")
public class OutComeAccountController {

    private static final Logger logger = LoggerFactory.getLogger(OutComeAccountController.class);

    @Autowired
    private OutComeAccountService outComeAccountService;

    @CircuitBreaker(name = "clientCB", fallbackMethod = "fallBackSaveOutComeAccountByDni")
    @PostMapping("/createOutComeAccountByDni")
    public ResponseEntity<Mono<OutComeAccount>> createOutComeAccountByDni(@RequestBody OutComeAccount outComeAccount){
        return outComeAccountService.createOutComeAccountByDni(outComeAccount);
    }

    @CircuitBreaker(name = "clientCB", fallbackMethod = "fallBackSaveOutComeAccountByRuc")
    @PostMapping("/createOutComeAccountByRuc")
    public ResponseEntity<Mono<OutComeAccount>> createOutComeAccountByRuc(@RequestBody OutComeAccount outComeAccount){
        return outComeAccountService.createOutComeAccountByRuc(outComeAccount);
    }

    @CircuitBreaker(name = "clientCB", fallbackMethod = "fallBackGetAccounts")
    @GetMapping("/findAccountByDni/{dni}")
    public ResponseEntity<Mono<List<BankAccount>>> getAccountsByDdni(@PathVariable ("dni") String dni){
        return outComeAccountService.findAccountsByDni(dni);
    }


    //------------------Implementation of Circuit Breaker
    private ResponseEntity<Mono<OutComeAccount>> fallBackSaveOutComeAccountByDni(@RequestBody OutComeAccount outComeAccount, RuntimeException e){
        logger.info("El DNI ".concat(outComeAccount.getIdentifier()).concat(" no está creado en la BD"));
        OutComeAccount outcomeAccount = new OutComeAccount();
        Mono<OutComeAccount> response = Mono.just(outcomeAccount);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    private ResponseEntity<Mono<OutComeAccount>> fallBackSaveOutComeAccountByRuc(@RequestBody OutComeAccount outComeAccount, RuntimeException e){
        logger.info("El RUC ".concat(outComeAccount.getIdentifier()).concat(" aún no está registrado en SUNARP"));
        OutComeAccount outcomeAccount = new OutComeAccount();
        Mono<OutComeAccount> response = Mono.just(outcomeAccount);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    private ResponseEntity<Mono<List<BankAccount>>> fallBackGetAccounts(@PathVariable ("dni") String dni, RuntimeException e){
        logger.info("El DNI ".concat(dni).concat(" se encuentra en la lista negra."));
        List<BankAccount> list = new ArrayList<>();
        Mono<List<BankAccount>> response = Mono.just(list);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
