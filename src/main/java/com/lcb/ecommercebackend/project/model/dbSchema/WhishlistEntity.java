package com.lcb.ecommercebackend.project.model.dbSchema;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
@Entity
@Table(name="whishlist")
public class WhishlistEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @JsonProperty("id")
    @Column(name = "id")
    private Long id;

    @JsonProperty("product_id")
    @Column(name = "product_id")
    private Long productId;

    @JsonProperty("user_id")
    @Column(name="user_id")
    private String userId;

    @JsonProperty("whishlist_id")
    @Column(name="whishlist_id")
    private String whishlistId;

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
