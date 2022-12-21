package com.bootcamp.project.bootcoinoperation.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BootcoinOperationDTO {
    private String petitionerDocumentNumber;
    private String paymentMethod;
    private String mobileNumber;
    private String accountNumber;
    private String sellerDocumentNumber;
    private double amount;
    private String status;
}
