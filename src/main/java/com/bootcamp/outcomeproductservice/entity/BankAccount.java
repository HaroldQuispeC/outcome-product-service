package com.bootcamp.outcomeproductservice.entity;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Data
@Getter
@Setter
public class BankAccount {

    private OutComeAccountType outComeAccountType;
    private Double currentBalance;
    private String status;
    private ArrayList<BankAccountOwner> bankAccountOwners;
    private ArrayList<BankAccountSigner> bankAccountSigners;

}
