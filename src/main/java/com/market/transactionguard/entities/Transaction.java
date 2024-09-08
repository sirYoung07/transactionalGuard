package com.market.transactionguard.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.swagger.v3.oas.annotations.servers.Server;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private BigDecimal amount;

    @Column(name = "recipient_email")
    private String recipientEmailAddress;

    @Column(name = "recipient_number")
    private String recipientPhoneNumber;

    @Column(name = "recipient_name")
    private String recipientName;

    @ManyToOne(cascade = CascadeType.ALL)
    @JsonBackReference
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id")
    private Product product;

    @Enumerated(EnumType.STRING)
    private TransactionStatus transactionStatus;

    public Transaction(BigDecimal amount, String recipientEmailAddress, String recipientPhoneNumber, String recipientName, Product product,TransactionStatus transactionStatus) {
        this.amount = amount;
        this.recipientEmailAddress = recipientEmailAddress;
        this.recipientPhoneNumber = recipientPhoneNumber;
        this.recipientName = recipientName;
        this.product = product;
        this.transactionStatus = transactionStatus;
    }




}
