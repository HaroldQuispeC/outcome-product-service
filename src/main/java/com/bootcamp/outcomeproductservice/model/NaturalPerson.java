package com.bootcamp.outcomeproductservice.model;

import lombok.Data;

@Data
public class NaturalPerson {
  private String idNaturalPerson;
  private String name;
  private String lastName;
  private String documentType;
  private String documentNumber;
  private String gender;
}
