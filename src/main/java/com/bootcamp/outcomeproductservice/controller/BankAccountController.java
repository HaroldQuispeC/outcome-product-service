package com.bootcamp.outcomeproductservice.controller;

import com.bootcamp.outcomeproductservice.entity.BankAccount;
import com.bootcamp.outcomeproductservice.service.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/bankAccount")
public class BankAccountController {

    @Autowired
    private BankService bankService;

    @PostMapping("/createBankAccount")
    private ResponseEntity<Mono<BankAccount>> createBankAccount(@RequestBody BankAccount bankAccount){
        return bankService.createBankAccount(bankAccount);
    }
}
