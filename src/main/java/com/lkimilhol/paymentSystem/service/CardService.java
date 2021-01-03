package com.lkimilhol.paymentSystem.service;

import com.lkimilhol.paymentSystem.domain.CardPayment;
import com.lkimilhol.paymentSystem.global.CardPaymentInfo;
import com.lkimilhol.paymentSystem.global.common.AES256Utility;
import com.lkimilhol.paymentSystem.global.common.CommonUtility;
import com.lkimilhol.paymentSystem.repository.CardPaymentRepository;
import com.lkimilhol.paymentSystem.repository.CardSendDataRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
public class CardService {

    private CardPaymentRepository cardPaymentRepository;
    private CardSendDataRepository cardSendDataRepository;

    CommonUtility commonUtility;
    AES256Utility aes256Utility;

    public CardService(CardPaymentRepository cp, CardSendDataRepository cs) {
        this.cardPaymentRepository = cp;
        this.cardSendDataRepository = cs;
        commonUtility = new CommonUtility();
        aes256Utility = new AES256Utility();
    }


    public String pay(CardPayment cardPayment) {
        cardPayment.setInsertTime(LocalDateTime.now());
        cardPaymentRepository.save(cardPayment);
        long cardPaymentId = cardPayment.getUniqueId();

        String encryptedCardInfo = aes256Utility.encryptCardInfo(cardPayment);
        System.out.println(encryptedCardInfo);


        int vatAmount = calculateVat(cardPayment.getAmount(), cardPayment.getVat());
        cardPayment.setAmount(cardPayment.getAmount() + vatAmount);


        return "";
    }

    private int calculateVat(int amount, int vat) {
        amount += vat == 0 ? Math.round((float) amount / 11) : vat;
        return amount;
    }



//    public Optional<CardPayment> findById(long id) {
//        return cardRepository.findById(id);
//    }

//    public List<CardPayment> findCardList() {
//        return cardRepository.findAll();
//    }
}
