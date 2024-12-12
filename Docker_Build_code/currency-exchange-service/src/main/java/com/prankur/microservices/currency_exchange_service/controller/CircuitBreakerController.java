package com.prankur.microservices.currency_exchange_service.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@RestController
public class CircuitBreakerController
{
	private Logger logger = LoggerFactory.getLogger(CircuitBreakerController.class);

	@GetMapping("/sample-api")
//	@Retry(name = "sample-api", fallbackMethod = "hardcodedResponse")
	@CircuitBreaker(name = "default", fallbackMethod = "hardcodedResponse")

	public String sampleApi()
	{
		logger.info("Sample Api call");
		new RestTemplate().getForEntity("http://localhost:8090/sdf", String.class);
		return "Sample API String";
	}

	public String hardcodedResponse(Exception ex)
	{
		return "This is hardcoded response after retries failure";
	}

}
