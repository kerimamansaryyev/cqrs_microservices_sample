package com.example.stockservice.controller;

import com.example.stockservice.response.StockSpotGetResponse;
import com.example.stockservice.service.StockQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class StockController {
    private final StockQueryService stockQueryService;

    @GetMapping(value = "/stock/{productId}")
    public StockSpotGetResponse getStockSpot(@PathVariable String productId){
        final var stockSpot = stockQueryService.getSpotOfProduct(productId);

        return new StockSpotGetResponse(
                        stockSpot == null? null: stockSpot.getStockNo(),
                        stockSpot == null? null: stockSpot.getAvailableQuantity()
        );
    }
}
