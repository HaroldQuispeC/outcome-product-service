package com.bootcamp.outcomeproductservice.controller;

import com.bootcamp.outcomeproductservice.entity.BankAccount;
import com.bootcamp.outcomeproductservice.entity.OutComeAccount;
import com.bootcamp.outcomeproductservice.service.OutComeAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/bankAccount")
public class OutComeAccountController {

    @Autowired
    private OutComeAccountService outComeAccountService;

    @PostMapping("/createOutComeAccountByDni")
    private ResponseEntity<Mono<OutComeAccount>> createOutComeAccountByDni(@RequestBody OutComeAccount outComeAccount){
        return outComeAccountService.createOutComeAccountByDni(outComeAccount);
    }

    @PostMapping("/createOutComeAccountByRuc")
    private ResponseEntity<Mono<OutComeAccount>> createOutComeAccountByRuc(@RequestBody OutComeAccount outComeAccount){
        return outComeAccountService.createOutComeAccountByRuc(outComeAccount);
    }

    @GetMapping("/findAccountByDni/{dni}")
    private ResponseEntity<Mono<List<BankAccount>>> getAccountsByDdni(@PathVariable ("dni") String dni){
        return outComeAccountService.findAccountsByDni(dni);
    }
}
