package com.lcb.ecommercebackend.project.repositories;

import com.lcb.ecommercebackend.project.model.dbSchema.OrderItemEntity;
import com.lcb.ecommercebackend.project.model.dbSchema.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItemEntity, Long> {
    public OrderItemEntity findByOrderItemId(String orderItemId); //which orderItemId belong to which order/item
    public List<OrderItemEntity> findByOrderId(Long orderId); //which orderId belong to what what orders/items
}
