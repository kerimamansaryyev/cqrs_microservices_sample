package com.example.productservice.service;

import com.example.productservice.model.Product;
import com.example.productservice.repository.ProductRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Data
@RequiredArgsConstructor
public class ProductQueryService {
    private final ProductRepository productRepository;

    public Product getProduct(String productId){
        return productRepository.findById(productId).orElse(null);
    }
}
