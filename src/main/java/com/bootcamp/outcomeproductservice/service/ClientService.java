package com.bootcamp.outcomeproductservice.service;

import com.bootcamp.outcomeproductservice.model.Client;
import com.bootcamp.outcomeproductservice.service.impl.ClientHystrixFallbackFactory;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(value="client",
            url = "http://localhost:8081/api/client", fallback = ClientHystrixFallbackFactory.class)
public interface ClientService {

    @GetMapping(value = "/")
    @Headers("Content-Type: application/json")
    public List<Client> getAll();

    @GetMapping(value = "/findByDocument/{dni}")
    @Headers("Content-Type: application/json")
    public Client findByDocumenNumber(@PathVariable("dni") String dni);

    @GetMapping(value="/findByRuc/{ruc}")
    @Headers("Content-Type: application/json")
    public Client findByRuc(@PathVariable("ruc") String ruc);
}
