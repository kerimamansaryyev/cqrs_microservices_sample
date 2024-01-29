package com.example.productservice.controller;

import com.example.productservice.external_response_mapper.StockSpotGetResponse;
import com.example.productservice.feign.StockFeignClient;
import com.example.productservice.request.ProductCreateRequest;
import com.example.productservice.request.ProductDeleteRequest;
import com.example.productservice.response.ErrorResponse;
import com.example.productservice.response.ProductCreatedResponse;
import com.example.productservice.response.ProductDeletedResponse;
import com.example.productservice.response.ProductGetResponse;
import com.example.productservice.service.ProductCommandService;
import com.example.productservice.service.ProductQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class ProductController {

    private final ProductCommandService commandService;
    private final ProductQueryService queryService;
    private final StockFeignClient stockFeignClient;

    @GetMapping(value = "/product/{productId}")
    public ResponseEntity<?> getProduct(@PathVariable String productId){
        final var product = queryService.getProduct(productId);
        if(product == null){
            return new ResponseEntity<>(
                    new ErrorResponse("The product wasn't found"),
                    HttpStatus.NOT_FOUND
            );
        }
        final var stockSpotRes = stockFeignClient.getStockSpot(productId);

        return new ResponseEntity<>(
                new ProductGetResponse(
                        product.getId(),
                        product.getPrice(),
                        product.getName(),
                        stockSpotRes.availableQuantity(),
                        stockSpotRes.stockNo()

                ),
                HttpStatus.OK
        );
    }

    @PostMapping(value = "/product/create")
    public ResponseEntity<?> createProduct(@RequestBody ProductCreateRequest request){
        final var product = commandService.createProduct(request.name(), request.price());
        return new ResponseEntity<>(
                new ProductCreatedResponse(
                        product.getId(),
                        product.getPrice(),
                        product.getName(),
                        product.getStockNo()
                ),
                HttpStatus.CREATED
        );
    }

    @PostMapping(value = "/product/delete")
    public ResponseEntity<?> deleteProduct(@RequestBody ProductDeleteRequest request){
        commandService.deleteProduct(request.productId());
        return new ResponseEntity<>(
                new ProductDeletedResponse(
                        "Product has successfully been deleted",
                        request.productId()
                ),
                HttpStatus.OK
        );
    }
}
