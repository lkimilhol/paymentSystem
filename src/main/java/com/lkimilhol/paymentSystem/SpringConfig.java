package com.lkimilhol.paymentSystem;

import com.lkimilhol.paymentSystem.repository.CardAdminRepository;
import com.lkimilhol.paymentSystem.repository.CardPaymentRepository;
import com.lkimilhol.paymentSystem.repository.CardSendDataRepository;
import com.lkimilhol.paymentSystem.service.*;
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
    public CardApiService cardApiService () {
        return new CardApiService();
    }

    @Bean
    public CardDataService cardDataService() {
        return new CardDataService();}

    @Bean
    public CardPaymentService cardPaymentService () {
        return new CardPaymentService(cardPaymentRepository());
    }

    @Bean
    public CardSendDataService cardSendDataService () {
        return new CardSendDataService(cardSendDataRepository());
    }

    @Bean
    public CardAdminService cardLogService() {
        return new CardAdminService(cardLogRepository());}

    @Bean
    public CardPaymentRepository cardPaymentRepository() {
        return new CardPaymentRepository(em);
    }

    @Bean
    public CardSendDataRepository cardSendDataRepository() {
        return new CardSendDataRepository(em);
    }

    @Bean
    public CardAdminRepository cardLogRepository() {
        return new CardAdminRepository(em);
    }

}
