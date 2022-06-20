package com.bootcamp.outcomeproductservice.service;

import com.bootcamp.outcomeproductservice.entity.BankAccount;
import com.bootcamp.outcomeproductservice.entity.OutComeAccount;
import java.util.List;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface OutComeAccountService {
  Flux<OutComeAccount> findAll();

  ResponseEntity<Mono<OutComeAccount>> createOutComeAccountByDni(OutComeAccount outComeAccount);

  ResponseEntity<Mono<OutComeAccount>> createOutComeAccountByRuc(OutComeAccount outComeAccount);

  Mono<List<BankAccount>> findAccountsByDni(String dni);

  Flux<BankAccount> findAccountsByRuc(String ruc);

  ResponseEntity<Mono<OutComeAccount>> addAccountOwner(
          String dni, String ruc, String accountSerialNumber);

  ResponseEntity<Mono<OutComeAccount>> addAccountSigner (
          String dni, String ruc, String accountSerialNumber
  );

}
