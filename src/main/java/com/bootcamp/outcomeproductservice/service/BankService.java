package com.bootcamp.outcomeproductservice.service;

import com.bootcamp.outcomeproductservice.entity.BankAccount;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

public interface BankService {

    ResponseEntity<Mono<BankAccount>> createBankAccount(BankAccount bankAccount);
}
