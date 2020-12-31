package com.lkimilhol.paymentSystem;

import com.lkimilhol.paymentSystem.repository.CardRepository;
import com.lkimilhol.paymentSystem.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;

@Configuration
public class SpringConfig {
    private EntityManager em;

    @Autowired
    public SpringConfig(EntityManager em) {
        this.em = em;
    }

    @Bean
    public CardService cardService() {
        return new CardService(cardRepository());}

    @Bean
    public CardRepository cardRepository() {
        return new CardRepository(this.em);}
}
