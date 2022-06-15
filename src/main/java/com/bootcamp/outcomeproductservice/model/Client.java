package com.bootcamp.outcomeproductservice.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
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
