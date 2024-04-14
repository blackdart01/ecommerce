package com.lcb.ecommercebackend.project.model.dbSchema;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.*;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
@Entity
@Table(name="reviews")
public class ReviewsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @JsonProperty("id")
    @Column(name = "id")
    private Long id;

    @JsonProperty("product_id")
    @Column(name = "product_id")
    private Long productId;

    @JsonProperty("review_id")
    @Column(name = "review_id")
    private Long reviewId;

    @JsonProperty("reviews_name")
    @Column(name="reviews_name")
    private String reviewsName;

    @JsonProperty("user_id")
    @Column(name="user_id")
    private String userId;

    @JsonProperty("rating")
    @Column(name="rating")
    private String rating;

    @JsonProperty("reviews")
    @Column(name="reviews")
    private String reviews; //it would be an array of strings

    @JsonProperty("supplier_id")
    @Column(name="supplier_id")
    private String supplierId;

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
