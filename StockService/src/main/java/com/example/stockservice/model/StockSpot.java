package com.example.stockservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@Builder
@Document(collection = "stock_spots")
public class StockSpot {
    @Id
    private String stockNo;
    private String productId;
    int availableQuantity;
}
