package com.bootcamp.outcomeproductservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Document(collection = "DebitCard")
public class DebitCard {
  @Id
  private String debitCardId;
  private String financialCompany;
  private String creditCardSn;
  private String expirationDate;
  private String cvv;
  private String status;
}
