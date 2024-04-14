package com.lcb.ecommercebackend.project.model.dbSchema;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
@Entity
@Table(name="order-item")
public class OrderItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @JsonProperty("id")
    @Column(name = "id")
    private Long id;

    @JsonProperty("order_id")
    @Column(name = "order_id")
    private Long orderId;

    @JsonProperty("product_id")
    @Column(name="product_id")
    private String productId;

    @JsonProperty("order_item_id")
    @Column(name="order_item_id")
    private String orderItemId;

    @JsonProperty("price")
    @Column(name="price")
    private String price;

    @JsonProperty("quantity")
    @Column(name="quantity")
    private String quantity;

    @JsonProperty("discount")
    @Column(name="discount")
    private String discount;

    @JsonProperty("created_at")
    @Column(name="created_at", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private ZonedDateTime createdAt;

    @JsonProperty("modified_at")
    @Column(name="modified_at", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private ZonedDateTime modifiedAt;
}
