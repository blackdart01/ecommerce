package com.lcb.ecommercebackend.project.repositories;

import com.lcb.ecommercebackend.project.model.dbSchema.ProductEntity;
import com.lcb.ecommercebackend.project.model.dbSchema.ReviewsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewsRepository extends JpaRepository<ReviewsEntity, Long> {
    public ReviewsEntity findByProductId(Long productId);
    public List<ReviewsEntity> findByUserId(String userId); //which user gave what what reviews
}
