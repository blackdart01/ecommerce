package com.lcb.ecommercebackend.project.repositories;

import com.lcb.ecommercebackend.project.model.dbSchema.DiscountEntity;
import com.lcb.ecommercebackend.project.model.dbSchema.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiscountRepository extends JpaRepository<DiscountEntity, Long> {
    public DiscountEntity findByProductId(Long productId); //which productId has what what discounts
    public DiscountEntity findByDiscountId(String discountId);
}
