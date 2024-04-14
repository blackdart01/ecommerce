package com.lcb.ecommercebackend.project.model.dbSchema;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
@Entity
@Table(name="product")
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @JsonProperty("id")
    @Column(name = "id")
    private Long id;

    @JsonProperty("product_id")
    @Column(name = "product_id")
    private Long productId;

    @JsonProperty("product_name")
    @Column(name="product_name")
    private String productName;

    @JsonProperty("product_description")
    @Column(name="product_description")
    private String productDesc;

    @JsonProperty("price")
    @Column(name="price")
    private String price;

    @JsonProperty("category_id")
    @Column(name="category_id")
    private Long categoryId;

    @JsonProperty("sku")
    @Column(name="sku")
    private String sku;

    @JsonProperty("supplier_id")
    @Column(name="supplier_id")
    private String supplierId;

    @JsonProperty("discount_id")
    @Column(name="discount_id")
    private String discountId;

    @JsonProperty("units_on_stock")
    @Column(name="units_on_stock")
    private String unitsOnStock;

    @JsonProperty("units_on_order")
    @Column(name="units_on_order")
    private String unitsOnOrder;

    @JsonProperty("is_active")
    @Column(name="is_active")
    private Boolean isActive;

    @JsonProperty("reviews_id")
    @Column(name="reviews_id")
    private String reviewsId;

    @JsonProperty("created_at")
    @Column(name="created_at", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private ZonedDateTime createdAt;

    @JsonProperty("modified_at")
    @Column(name="modified_at", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private ZonedDateTime modifiedAt;
}
