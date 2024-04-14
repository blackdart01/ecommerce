package com.lcb.ecommercebackend.project.model.dbSchema;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
@Entity
@Table(name="user-cart")
public class CartsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @JsonProperty("id")
    @Column(name = "id")
    private Long id;

    @JsonProperty("product_id")
    @Column(name = "product_id")
    private Long productId;

    @JsonProperty("cart_id")
    @Column(name="cart_id")
    private String cartId;

    @JsonProperty("quantity")
    @Column(name="quantity")
    private String quantity;

    @JsonProperty("is_whishlisted")
    @Column(name="is_whishlisted")
    private Boolean isWhishlisted;

    @JsonProperty("created_at")
    @Column(name="created_at", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private ZonedDateTime createdAt;

    @JsonProperty("modified_at")
    @Column(name="modified_at", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private ZonedDateTime modifiedAt;
}
