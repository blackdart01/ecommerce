package com.lcb.ecommercebackend.project.model.dbSchema;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
@Entity
@Table(name="discount")
public class DiscountEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @JsonProperty("id")
    @Column(name = "id")
    private Integer id;

    @NotNull
    @JsonProperty("product_id")
    @Column(name = "product_id")
    private String productId;

    @NotNull
    @JsonProperty("discounted-price")
    @Column(name="discounted-price")
    private String discountedPrice;

    @JsonProperty("discount_id")
    @Column(name="discount_id")
    private String discountId;

    @NotNull
    @JsonProperty("is_active")
    @Column(name="is_active")
    private Boolean isActive;

    @NotNull
    @JsonProperty("created_at")
    @Column(name="created_at", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private ZonedDateTime createdAt;

    @JsonProperty("modified_at")
    @Column(name="modified_at", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private ZonedDateTime modifiedAt;
}
