package com.bootcamp.outcomeproductservice.repository;

import com.bootcamp.outcomeproductservice.entity.OutComeAccountType;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface OutComeAccountTypeRepository extends
        ReactiveMongoRepository<OutComeAccountType,String> {
}
