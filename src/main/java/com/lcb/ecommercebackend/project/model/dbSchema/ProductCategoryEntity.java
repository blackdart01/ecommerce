package com.lcb.ecommercebackend.project.model.dbSchema;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
@Entity
@Table(name="product_category")
public class ProductCategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @JsonProperty("id")
    @Column(name = "id")
    private Long id;

    @JsonProperty("category_id")
    @Column(name="category_id")
    public Long categoryId;

    @JsonProperty("category_name")
    @Column(name="category_name")
    public String categoryName;

    @JsonProperty("category_desc")
    @Column(name="category_desc")
    public String categoryDescription;

    @JsonProperty("created_at")
    @Column(name="created_at", columnDefinition = "TIMESTAMP WITH TIME ZONE", nullable = false)
    private ZonedDateTime createdAt;

    @JsonProperty("modified_at")
    @Column(name="modified_at", columnDefinition = "TIMESTAMP WITH TIME ZONE", nullable = false)
    private ZonedDateTime modifiedAt;

    @JsonProperty("deleted_at")
    @Column(name="deleted_at", columnDefinition = "TIMESTAMP WITH TIME ZONE", nullable = false)
    private ZonedDateTime deletedAt;
}