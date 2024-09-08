package com.market.transactionguard.entities;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "product_details")
    private String productDetails;

    @Column(name = "product_description")
    private String productDescription;

    @Column(name = "product_image")
    private String productImages;


    public Product(String productName, String productDetails, String productDescription, String productImage) {
    }

    public Product(String productName, String productDetails, String productDescription) {
    }
}
