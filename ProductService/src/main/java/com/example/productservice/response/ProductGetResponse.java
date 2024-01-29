package com.example.productservice.response;

public record ProductGetResponse(String productId, double price, String name, int availableQuantity, String stockNo) {
}
