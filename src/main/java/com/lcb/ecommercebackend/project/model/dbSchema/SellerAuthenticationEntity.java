package com.lcb.ecommercebackend.project.model.dbSchema;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.List;

@Data
@Entity
@Table(name="seller-auth")
public class SellerAuthenticationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @JsonProperty("id")
    @Column(name = "id")
    private Integer id;

    @JsonProperty("supplier_id")
    @Column(name = "supplier_id")
    private String supplierId;

    @JsonProperty("user_name")
    @Column(name = "user_name")
    private String userName;

    @JsonProperty("supplier_name")
    @Column(name = "supplier_name")
    private String supplierName;

    @JsonProperty("contact_email")
    @Column(name = "contact_email")
    private String contactEmail;

    @JsonProperty("contact_phone")
    @Column(name = "contact_phone")
    private String contactPhone;

    @JsonProperty("is_active")
    @Column(name = "is_active")
    private Boolean isActive;

    @JsonProperty("created_at")
    @Column(name = "created_at", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private ZonedDateTime createdAt;

    @JsonProperty("modified_at")
    @Column(name = "modified_at", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private ZonedDateTime modifiedAt;
}
