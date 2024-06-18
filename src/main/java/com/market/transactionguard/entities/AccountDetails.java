package com.market.transactionguard.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "account_details")
public class AccountDetails implements Serializable
{
  //  @Serial
   // private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_detail_id")
    private Long accountDetailsId;

    @Column(name = "bank_code")
    private String bankCode;

    @Column(name = "bank_name")
    private String bankName;

    @Column(name = "account_number")
    private String accountNumber;

    @Column(name = "account_name")
    private String accountName;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    @JsonBackReference
    private Account account;


//    @Override
//    public String toString() {
//        return "AccountDetails{" +
//            "accountDetailsId=" + accountDetailsId +
//            ", bankCode='" + bankCode + '\'' +
//            ", bankName='" + bankName + '\'' +
//            ", accountNumber='" + accountNumber + '\'' +
//            ", accountName='" + accountName + '\'' +
//            ", account=" + account +
//            '}';
//    }
}
