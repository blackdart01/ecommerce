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
@Table(name="supplier")
public class SupplierEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @JsonProperty("id")
    @Column(name = "id")
    private Integer id;

    @JsonProperty("supplier_id")
    @Column(name = "supplier_id") // Mark product_id as unique
    private String supplierId;

    @NotNull
    @NotBlank
    @JsonProperty("user_name")
    @Column(name = "user_name")
    private String userName;

    @NotNull
    @NotBlank
    @JsonProperty("supplier_name")
    @Column(name = "supplier_name")
    private String supplierName;

    @JsonProperty("contact_email")
    @Column(name = "contact_email")
    private String contactEmail;

    @JsonProperty("contact_phone")
    @Column(name = "contact_phone")
    private String contactPhone;

    @JsonProperty("address_line1")
    @Column(name = "address_line1", length = 255)
    private String addressLine1;

    @JsonProperty("address_line2")
    @Column(name = "address_line2", length = 255)
    private String addressLine2;

    @JsonProperty("city")
    @Column(name = "city", length = 100)
    private String city;

    @JsonProperty("state")
    @Column(name = "state", length = 100)
    private String state;

    @JsonProperty("postal_code")
    @Column(name = "postal_code", length = 20)
    private String postalCode;

    @JsonProperty("country")
    @Column(name = "country", length = 100)
    private String country;

    @JsonProperty("products")
    @Column(name = "products")
//    @OneToMany(mappedBy = "supplier", cascade = CascadeType.ALL) // OneToMany relationship with Product
    private List<String> products;

    @NotNull
    @JsonProperty("is_active")
    @Column(name = "is_active")
    private Boolean isActive;

    @JsonProperty("reviews_id")
    @Column(name = "reviews_id")
    private String reviewsId; // Consider using a relationship with a Review entity

    @JsonProperty("created_at")
    @Column(name = "created_at", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private ZonedDateTime createdAt;

    @JsonProperty("modified_at")
    @Column(name = "modified_at", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private ZonedDateTime modifiedAt;
}
