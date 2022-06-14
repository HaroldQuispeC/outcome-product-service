package com.bootcamp.outcomeproductservice.service;

import com.bootcamp.outcomeproductservice.entity.BankAccount;
import com.bootcamp.outcomeproductservice.entity.OutComeAccount;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface OutComeAccountService {
    Flux<OutComeAccount> findAll();
    ResponseEntity<Mono<OutComeAccount>> createOutComeAccountByDni(OutComeAccount outComeAccount);
    ResponseEntity<Mono<OutComeAccount>> createOutComeAccountByRuc (OutComeAccount outComeAccount);
    ResponseEntity<Mono<List<BankAccount>>> findAccountsByDni(String dni);
}
