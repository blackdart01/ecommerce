package com.lcb.ecommercebackend.project.repositories;

import com.lcb.ecommercebackend.project.model.dbSchema.ProductEntity;
import com.lcb.ecommercebackend.project.model.dbSchema.WhishlistEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WhishlistRepository extends JpaRepository<WhishlistEntity, Integer> {
    public List<WhishlistEntity> findByUserId(String userId);
    public WhishlistEntity findByWhishlistId(String whishlistId);
}
