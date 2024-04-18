package com.lcb.ecommercebackend.project.repositories;

import com.lcb.ecommercebackend.project.model.dbSchema.CartsEntity;
import com.lcb.ecommercebackend.project.model.dbSchema.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartsRepository extends JpaRepository<CartsEntity, Integer> {
    public CartsEntity findByCartId(String cartId); //which cartId has what items in cart
}