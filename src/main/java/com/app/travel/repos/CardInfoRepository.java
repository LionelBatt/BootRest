package com.app.travel.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.travel.model.CardInfo;

@Repository
public interface CardInfoRepository extends JpaRepository<CardInfo, Integer> {
}
