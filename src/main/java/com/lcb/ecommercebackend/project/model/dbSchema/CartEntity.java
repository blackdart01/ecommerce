package com.lcb.ecommercebackend.project.model.dbSchema;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
@Entity
@Table(name="cart_details")
public class CartEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @JsonProperty("id")
    @Column(name = "id")
    private Integer id;

    @JsonProperty("user_id")
    @Column(name = "user_id")
    private Integer userId;

    @JsonProperty("cart_id")
    @Column(name="cart_id")
    private String cartId;

    @JsonProperty("price")
    @Column(name="price")
    private String price;

    @JsonProperty("net-quantity")
    @Column(name="net-quantity")
    private String netQuantity;

    @JsonProperty("delivery-charges")
    @Column(name="delivery-charges")
    private String deliveryCharges;

    @JsonProperty("total-amount")
    @Column(name="total-amount")
    private String totalAmount;

    @JsonProperty("discount_price")
    @Column(name="discount_price")
    private String discountPrice;

    @JsonProperty("created_at")
    @Column(name="created_at", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private ZonedDateTime createdAt;

    @JsonProperty("modified_at")
    @Column(name="modified_at", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private ZonedDateTime modifiedAt;
}
