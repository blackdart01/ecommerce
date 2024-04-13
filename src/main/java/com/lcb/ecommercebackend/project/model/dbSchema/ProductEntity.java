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

    @JsonProperty("created_at")
    @Column(name="created_at", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private ZonedDateTime createdAt;

    @JsonProperty("modified_at")
    @Column(name="modified_at", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private ZonedDateTime modifiedAt;

    @JsonProperty("deleted_at")
    @Column(name="deleted_at", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private ZonedDateTime deletedAt;
}
