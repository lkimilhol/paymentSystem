package com.lkimilhol.paymentSystem.repository;

import com.lkimilhol.paymentSystem.domain.Card;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
public
interface CardRepository extends JpaRepository<Card, Long> {
    @Override
    <S extends Card> S save(S entity);

    @Override
    List<Card> findAll();

    @Override
    void deleteAll();
}
