package com.bootcamp.outcomeproductservice.repository;

import com.bootcamp.outcomeproductservice.entity.BankAccount;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankAccountRepository extends ReactiveMongoRepository<BankAccount,String> {
}
