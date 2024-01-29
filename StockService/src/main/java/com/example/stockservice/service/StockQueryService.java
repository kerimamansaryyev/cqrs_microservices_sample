package com.example.stockservice.service;

import com.example.stockservice.model.StockSpot;
import com.example.stockservice.repository.StockSpotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StockQueryService {
    private final StockSpotRepository stockSpotRepository;

    public StockSpot getSpotOfProduct(String productId){
        return stockSpotRepository.findByProductId(productId).orElse(null);
    }
}
