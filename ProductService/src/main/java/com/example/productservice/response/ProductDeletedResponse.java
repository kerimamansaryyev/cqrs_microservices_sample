package com.example.productservice.response;

import lombok.RequiredArgsConstructor;


public record ProductDeletedResponse(String message, String productId){
}
