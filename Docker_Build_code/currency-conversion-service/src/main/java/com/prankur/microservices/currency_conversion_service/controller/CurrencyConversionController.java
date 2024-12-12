package com.prankur.microservices.currency_conversion_service.controller;

import java.math.BigDecimal;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.prankur.microservices.currency_conversion_service.entities.CurrencyConversion;
import com.prankur.microservices.currency_conversion_service.proxies.CurrencyExchangeProxy;

@Configuration(proxyBeanMethods = false)
class RestTemplateConfiguration
{
	@Bean
	RestTemplate restTemplate(RestTemplateBuilder builder)
	{
		return builder.build();
	}
}

@RestController
public class CurrencyConversionController
{
	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private CurrencyExchangeProxy proxy;

	@GetMapping("/currency-conversion/from/{from}/to/{to}/quantity/{quantity}")
	public CurrencyConversion calculateCurrencyConversion(@PathVariable String from, @PathVariable String to,
			@PathVariable BigDecimal quantity)
	{
		HashMap<String, String> uriVariables = new HashMap<>();
		uriVariables.put("from", from);
		uriVariables.put("to", to);

		ResponseEntity<CurrencyConversion> response = restTemplate.getForEntity(
				"http://localhost:8000/currency-exchange/from/{from}/to/{to}", CurrencyConversion.class, uriVariables);
		CurrencyConversion currencyConversion = response.getBody();
		currencyConversion.setQuantity(quantity);
		currencyConversion.setTotalCalculatedAmount(currencyConversion.getConversionMultiple().multiply(quantity));
		currencyConversion.setEnvironment(currencyConversion.getEnvironment() + " from RestTemplate;");

		return currencyConversion;
	}

	@GetMapping("/currency-conversion-feign/from/{from}/to/{to}/quantity/{quantity}")
	public CurrencyConversion calculateCurrencyConversionFeign(@PathVariable String from, @PathVariable String to,
			@PathVariable BigDecimal quantity)
	{

		CurrencyConversion currencyConversion = proxy.retrieveExchangeValue(from, to);
		currencyConversion.setQuantity(quantity);
		currencyConversion.setTotalCalculatedAmount(currencyConversion.getConversionMultiple().multiply(quantity));
		currencyConversion.setEnvironment("Port " + currencyConversion.getEnvironment() + " from Feign;");

		return currencyConversion;
	}

}
