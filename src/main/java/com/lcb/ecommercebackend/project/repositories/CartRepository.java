package com.lcb.ecommercebackend.project.repositories;

import com.lcb.ecommercebackend.project.model.dbSchema.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<CartEntity, Long> {
    public List<CartEntity> findByUserId(Long userId); //which user get which cart
    public CartEntity findByCartId(String cartId);
}
