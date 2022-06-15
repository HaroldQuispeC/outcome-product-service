package com.bootcamp.outcomeproductservice.service.impl;

import com.bootcamp.outcomeproductservice.model.Business;
import com.bootcamp.outcomeproductservice.model.Client;
import com.bootcamp.outcomeproductservice.model.NaturalPerson;
import com.bootcamp.outcomeproductservice.service.ClientService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ClientHystrixFallbackFactory implements ClientService {
    @Override
    public List<Client> getAll() {
        List<Client> clients = new ArrayList<>();
        return clients;
    }

    Business business = new Business();
    NaturalPerson naturalPerson = new NaturalPerson();

    @Override
    public Client findByDocumenNumber(String dni) {
        Client client = Client.builder()
                .clientType("none")
                .address("none")
                .country("none")
                .joiningDate("none")
                .status("none")
                .naturalPerson(naturalPerson)
                .country("none")
                .build();

        return client;
    }

    @Override
    public Client findByRuc(String ruc) {
        Client client = Client.builder()
                .clientType("none")
                .address("none")
                .country("none")
                .joiningDate("none")
                .status("none")
                .business(business)
                .country("none")
                .build();
        return client;
    }
}
