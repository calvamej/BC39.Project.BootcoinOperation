package com.bootcamp.project.bootcoinoperation.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "BootcoinOperation")
public class BootcoinOperationEntity {
    @Id
    private String id;
    private String petitionerDocumentNumber;
    private String paymentMethod;
    private double amount;
    private String petitionerMobileNumber;
    private String petitionerAccountNumber;
    private String status;
    private boolean initialValidations;
    private boolean validated;
    private String sellerDocumentNumber;
    private String operationNumber;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date createDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date modifyDate;
}
