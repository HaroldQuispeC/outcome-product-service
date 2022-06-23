package com.bootcamp.outcomeproductservice.service;

import com.bootcamp.outcomeproductservice.exceptions.ModelException;
import com.bootcamp.outcomeproductservice.model.Client;
import feign.Headers;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

//@FeignClient(value = "client",
//        url = "http://localhost:8081/api/client")
@FeignClient(name = "GATEWAY-SERVICE/api/client",
        fallback = ClientService.ClientServiceFallback.class)
public interface ClientService {

  @GetMapping(value = "/findByDocument/{dni}")
  @CircuitBreaker(name = "client-service-cb")
  @Headers("Content-Type: application/json")
  public Client findByDocumentNumber(@PathVariable("dni") String dni);

  @GetMapping(value = "/findByRuc/{ruc}")
  @Headers("Content-Type: application/json")
  @CircuitBreaker(name = "client-service-cb")
  public Client findByRuc(@PathVariable("ruc") String ruc);

  @Component
  class ClientServiceFallback implements ClientService {

    private final String defaultMessage = "Client Service is unavailable at this moment";

    /**
     * findByDocument fallback method.
     *
     * @param dni String
     * @return String
     */
    @Override
    public Client findByDocumentNumber(String dni) {
      throw new ModelException(String.format("findByDocument: %s", defaultMessage));
    }

    /**
     * findByRuc fallback method.
     *
     * @param ruc String
     * @return Client
     */
    @Override
    public Client findByRuc(String ruc) {
      throw new ModelException(String.format("findByRuc: %s", defaultMessage));
    }
  }
}
