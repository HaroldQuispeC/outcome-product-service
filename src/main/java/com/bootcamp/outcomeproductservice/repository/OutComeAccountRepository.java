package com.bootcamp.outcomeproductservice.repository;

import com.bootcamp.outcomeproductservice.entity.BankAccount;
import com.bootcamp.outcomeproductservice.entity.OutComeAccount;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OutComeAccountRepository extends ReactiveMongoRepository<OutComeAccount,String> {
}
