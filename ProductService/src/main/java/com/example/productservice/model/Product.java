package com.example.productservice.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Document(collection = "products")
public class Product {
    @Id
    private String id;
    private String name;
    private double price;
    private String stockNo;
}
