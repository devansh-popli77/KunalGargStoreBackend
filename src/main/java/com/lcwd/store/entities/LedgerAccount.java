package com.lcwd.store.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LedgerAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String accountCode;
    private String gstNo;
    private String accountName;
    private String address;
    private String city;
    private String pincode;
    private String state;
    private String openingBalance;
    private String msmedStatus;
    private String contactNo;
    private String email;
    private String pan;
    private String turnoverBelow10Cr;
    private String approved;
    private String accountNum;
    private String accountNameBank;
    private String ifsc;
    private String branch;
    @Column(name = "is_deleted")
    private boolean isDeleted = false;

    // Constructors, getters, and setters
}
