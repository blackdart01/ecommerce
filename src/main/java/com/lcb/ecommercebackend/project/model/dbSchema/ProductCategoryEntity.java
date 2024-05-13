package com.lcb.ecommercebackend.project.model.dbSchema;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.List;

@Data
@Entity
@Table(name="product_category")
public class ProductCategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @JsonProperty("id")
    @Column(name = "id")
    private Integer id;

    @JsonProperty("category_id")
    @Column(name="category_id")
    public String categoryId;

    @NotNull
    @JsonProperty("category_name")
    @Column(name="category_name")
    public String categoryName;

    @NotNull
    @JsonProperty("category_desc")
    @Column(name="category_desc")
    public String categoryDescription;

    @JsonProperty("product_list")
    @Column(name="product_list")
    public List<String> productList;

    @NotNull
    @JsonProperty("is_active")
    @Column(name="is_active")
    private Boolean isActive;

    @JsonProperty("created_at")
    @Column(name="created_at", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private ZonedDateTime createdAt;

    @JsonProperty("modified_at")
    @Column(name="modified_at", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private ZonedDateTime modifiedAt;
}
