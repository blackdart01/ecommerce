package com.lcb.ecommercebackend.project.model.dbSchema;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
    private Integer id;

    @NotNull
    @JsonProperty("product_id")
    @Column(name = "product_id", unique = true) // Mark product_id as unique
    private String productId;

    @NotNull
    @JsonProperty("product_name")
    @Column(name = "product_name")
    private String productName;

    @JsonProperty("product_description")
    @Column(name = "product_description")
    private String productDesc;

    @NotNull
    @JsonProperty("price")
    @Column(name = "price")
    private String price; // Consider using a numeric type (e.g., BigDecimal) for currency

    @JsonProperty("category_id")
    @Column(name = "category_id")
    private Integer categoryId;

    @NotNull
    @JsonProperty("sku")
    @Column(name = "sku")
    private String sku;

    @NotNull
    @JsonProperty("supplier_id")
    @Column(name = "supplier_id")
    private String supplierId;

    @JsonProperty("discount_id")
    @Column(name = "discount_id")
    private String discountId; // Consider using a relationship with a Discount entity

    @NotNull
    @JsonProperty("units_on_stock")
    @Column(name = "units_on_stock")
    private Integer unitsOnStock; // Consider using an integer type for stock

    @NotNull
    @JsonProperty("units_on_order")
    @Column(name = "units_on_order")
    private Integer unitsOnOrder; // Consider using an integer type for orders

    @NotNull
    @JsonProperty("is_active")
    @Column(name = "is_active")
    private Boolean isActive;

    @JsonProperty("reviews_id")
    @Column(name = "reviews_id")
    private String reviewsId; // Consider using a relationship with a Review entity

    @NotNull
    @JsonProperty("created_at")
    @Column(name = "created_at", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private ZonedDateTime createdAt;

    @JsonProperty("modified_at")
    @Column(name = "modified_at", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private ZonedDateTime modifiedAt;

//    public ProductEntity() {
//    }

//    public String getProductId() {
//        return productId;
//    }
//
//    public void setProductId(String productId) {
//        this.productId = generateUniqueProductId();
//    }

//    public ProductEntity(String productName, String productDesc, String price, Long categoryId, String sku, String supplierId, String discountId, Integer unitsOnStock, Integer unitsOnOrder, Boolean isActive, String reviewsId, ZonedDateTime createdAt, ZonedDateTime modifiedAt) {
//        this.productId = generateUniqueProductId();
//        this.productName = productName;
//        this.productDesc = productDesc;
//        this.price = price;
//        this.categoryId = categoryId;
//        this.sku = sku;
//        this.supplierId = supplierId;
//        this.discountId = discountId;
//        this.unitsOnStock = unitsOnStock;
//        this.unitsOnOrder = unitsOnOrder;
//        this.isActive = isActive;
//        this.reviewsId = reviewsId;
//        this.createdAt = createdAt;
//        this.modifiedAt = modifiedAt;
//    }

}
