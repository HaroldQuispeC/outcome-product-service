package com.bootcamp.outcomeproductservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/outcomeProduct")
public class OutcomeProductController {
    @GetMapping("/")
    public String getSaludar(){

        return "Hola";
    }
}
