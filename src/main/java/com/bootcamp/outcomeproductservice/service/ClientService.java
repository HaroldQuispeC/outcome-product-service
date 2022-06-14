package com.bootcamp.outcomeproductservice.service;

import com.bootcamp.outcomeproductservice.model.Client;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@FeignClient(value="client",
            url = "http://localhost:8081/api/client")
public interface ClientService {

    @RequestMapping(method = RequestMethod.GET, value = "/")
    @Headers("Content-Type: application/json")
    public List<Client> getAll();

    @RequestMapping(method = RequestMethod.GET, value = "/findByDocument/{dni}")
    @Headers("Content-Type: application/json")
    public Client findByDocumenNumber(@PathVariable("dni") String dni);

    @RequestMapping(method = RequestMethod.GET,value="/findByRuc/{ruc}")
    @Headers("Content-Type: application/json")
    public Client findByRuc(@PathVariable("ruc") String ruc);
}
