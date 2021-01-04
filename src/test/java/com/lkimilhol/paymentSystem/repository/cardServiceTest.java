package com.lkimilhol.paymentSystem.repository;

import com.lkimilhol.paymentSystem.domain.CardPayment;
import com.lkimilhol.paymentSystem.service.CardService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;


import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Rollback
public class cardServiceTest {

    @Autowired
    private CardService cardService;

}
