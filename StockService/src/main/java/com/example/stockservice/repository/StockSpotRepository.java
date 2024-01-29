package com.example.stockservice.repository;

import com.example.stockservice.model.StockSpot;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StockSpotRepository extends MongoRepository<StockSpot, String> {
    Optional<StockSpot> findByProductId(String productId);
}
