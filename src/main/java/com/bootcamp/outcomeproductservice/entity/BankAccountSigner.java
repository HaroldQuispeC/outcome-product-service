package com.bootcamp.outcomeproductservice.entity;

import com.bootcamp.outcomeproductservice.model.Client;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class BankAccountSigner {

    private String status;
    private Client client;
}
