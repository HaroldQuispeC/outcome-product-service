package com.bootcamp.outcomeproductservice.entity;

import com.bootcamp.outcomeproductservice.model.Client;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Getter
@Setter
@Document(collection = "BankAccount_Prueba")
public class BankAccount {

    @Id
    private String accountId;
    private String startDate;
    private String endDate;
    private String Status;
    private String identifier;

    private OutComeAccountType outComeAccountType;

}
