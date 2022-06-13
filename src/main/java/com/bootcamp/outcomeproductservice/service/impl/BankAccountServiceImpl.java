package com.bootcamp.outcomeproductservice.service.impl;

import com.bootcamp.outcomeproductservice.entity.BankAccount;
import com.bootcamp.outcomeproductservice.model.Client;
import com.bootcamp.outcomeproductservice.repository.BankAccountRepository;
import com.bootcamp.outcomeproductservice.service.BankService;
import com.bootcamp.outcomeproductservice.service.ClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;


@Service
public class BankAccountServiceImpl implements BankService {

    private static final Logger logger = LoggerFactory.getLogger(BankAccountServiceImpl.class);

    @Autowired
    private ClientService clientService;

    private String url;

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Override
    public ResponseEntity<Mono<BankAccount>> createBankAccount(BankAccount bankAccount) {
        logger.info("PROCESS TO CREATE BANKACCOUNT");

        List<Client> clientes = clientService.getAll();
        clientes.forEach(System.out::println);

        return new ResponseEntity<>(bankAccountRepository.save(bankAccount),HttpStatus.OK);
    }
}
