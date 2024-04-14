package com.lcb.ecommercebackend.project.repositories;

import com.lcb.ecommercebackend.project.model.dbSchema.OrdersEntity;
import com.lcb.ecommercebackend.project.model.dbSchema.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdersRepository extends JpaRepository<OrdersEntity, Long> {
    public OrdersEntity findByOrderId(Long orderId); // which orderId belong to which order
    public OrdersEntity findByUserId(String userId); //which user placed which order
}
