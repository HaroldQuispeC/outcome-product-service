package com.bootcamp.outcomeproductservice.entity;

import com.bootcamp.outcomeproductservice.model.Client;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;

@Data
@Getter
@Setter
@Document(collection = "BankAccount_Prueba")
public class OutComeAccount {

    @Id
    private String idOutComeAccount;
    private String startDate;
    private String endDate;
    private String identifier;

    private Client client;
    private ArrayList<BankAccount> bankAccounts;
}
