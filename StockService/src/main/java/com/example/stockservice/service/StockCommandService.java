package com.example.stockservice.service;

import com.example.stockservice.event.ProductCreatedEvent;
import com.example.stockservice.event.ProductDeletedEvent;
import com.example.stockservice.event.ProductRegisteredInStock;
import com.example.stockservice.integration.AMQMessageSender;
import com.example.stockservice.model.StockSpot;
import com.example.stockservice.repository.StockSpotRepository;
import com.example.stockservice.util.AMQMessageMapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Data
@RequiredArgsConstructor
public class StockCommandService {
    private final StockSpotRepository stockSpotRepository;
    private final AMQMessageSender messageSender;
    @Value("${product-created-channel}")
    private String productCreatedChannel;
    @Value("${product-deleted-channel}")
    private String productDeletedChannel;
    @Value("${stock-registered-channel}")
    private String stockRegisteredChannel;

    @JmsListener(destination = "${product-created-channel}")
    @Transactional
    public void onProductCreated(String messageAsString) {
        try {
            ProductCreatedEvent event = AMQMessageMapper.asMessage(messageAsString, ProductCreatedEvent.class);
            var stockSpot =StockSpot.builder()
                    .productId(event.productId())
                    .availableQuantity(0)
                    .build();
            stockSpot = stockSpotRepository.save(stockSpot);
            messageSender.sendMessage(
                    new ProductRegisteredInStock(stockSpot.getProductId(), stockSpot.getStockNo()),
                    stockRegisteredChannel
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @JmsListener(destination = "${product-deleted-channel}")
    @Transactional
    public void onProductDeleted(String messageAsString) {
        try {
            ProductDeletedEvent event = AMQMessageMapper.asMessage(messageAsString, ProductDeletedEvent.class);
            final var stockSpot = stockSpotRepository.findByProductId(event.productId());
            stockSpot.ifPresent(
                    s -> stockSpotRepository.deleteById(s.getStockNo())
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
