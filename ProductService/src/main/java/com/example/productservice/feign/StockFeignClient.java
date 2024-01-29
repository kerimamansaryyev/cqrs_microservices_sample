package com.example.productservice.feign;

import com.example.productservice.external_response_mapper.StockSpotGetResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "stock-service")
@Component
public interface StockFeignClient {

    @GetMapping("/stock/{productNumber}")
    StockSpotGetResponse getStockSpot(@PathVariable String productNumber);
}
