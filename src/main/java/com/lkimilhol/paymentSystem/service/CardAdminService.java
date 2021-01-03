package com.lkimilhol.paymentSystem.service;

import com.lkimilhol.paymentSystem.domain.CardAdmin;
import com.lkimilhol.paymentSystem.repository.CardAdminRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CardAdminService {
    private CardAdminRepository cardAdminRepository;

    public CardAdminService(CardAdminRepository cardAdminRepository) {
        this.cardAdminRepository = cardAdminRepository;
    }

    public CardAdmin save(CardAdmin cardAdmin) {
        cardAdminRepository.save(cardAdmin);
        return cardAdmin;
    }

    public Optional<CardAdmin> findByUniqueId(String uniqueId) {
        return cardAdminRepository.findByUniqueId(uniqueId);
    }
}
