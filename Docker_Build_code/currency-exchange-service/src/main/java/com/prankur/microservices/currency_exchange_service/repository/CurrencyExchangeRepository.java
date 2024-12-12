package com.prankur.microservices.currency_exchange_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.prankur.microservices.currency_exchange_service.entities.CurrencyExchange;

@Repository
public interface CurrencyExchangeRepository extends JpaRepository<CurrencyExchange, Long>
{
	CurrencyExchange findByFromAndTo(String from, String to);
}
