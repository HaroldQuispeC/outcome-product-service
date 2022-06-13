package com.bootcamp.outcomeproductservice.model;

import lombok.Data;

@Data
public class Client {

    private String idClient;
    private String joiningDate;
    private String country;
    private String address;
    private String clientType;
    private String status;

    private NaturalPerson naturalPerson;

    private Business business;
}
