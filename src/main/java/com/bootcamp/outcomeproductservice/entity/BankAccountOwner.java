package com.bootcamp.outcomeproductservice.entity;

import com.bootcamp.outcomeproductservice.model.Client;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class BankAccountOwner {

  private String status;
  private Client client;
}
