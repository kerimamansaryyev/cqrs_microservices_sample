package com.example.productservice.service;

import com.example.productservice.event.ProductCreatedEvent;
import com.example.productservice.event.ProductDeletedEvent;
import com.example.productservice.event.ProductRegisteredInStock;
import com.example.productservice.integration.AMQMessageSender;
import com.example.productservice.model.Product;
import com.example.productservice.repository.ProductRepository;
import com.example.productservice.util.AMQMessageMapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Data
@RequiredArgsConstructor
public class ProductCommandService {
    private final ProductRepository productRepository;
    private final AMQMessageSender messageSender;
    @Value("${product-created-channel}")
    private String productCreatedChannel;
    @Value("${product-deleted-channel}")
    private String productDeletedChannel;
    @Value("${stock-registered-channel}")
    private String stockRegisteredChannel;

    @JmsListener(destination = "${stock-registered-channel}")
    @Transactional
    public void onRegisterInStock(String messageAsString){
        try {
            ProductRegisteredInStock event = AMQMessageMapper.asMessage(messageAsString, ProductRegisteredInStock.class);
            productRepository.findById(event.productId()).ifPresent(
                    p -> {
                        p.setStockNo(event.stockId());
                        productRepository.save(p);
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Product createProduct(String name, double price){
        var product = Product.builder()
                .price(price)
                .name(name)
                .build();

        product=productRepository.save(product);

        messageSender.sendMessage(
                new ProductCreatedEvent(
                        product.getId()
                ),
                productCreatedChannel
        );

        return product;
    }

    public void deleteProduct(String productId){
        productRepository.deleteById(productId);
        messageSender.sendMessage(
                new ProductDeletedEvent(
                        productId
                ),
                productDeletedChannel
        );
    }
}
