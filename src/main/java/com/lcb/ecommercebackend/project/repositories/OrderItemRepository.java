package com.lcb.ecommercebackend.project.repositories;

import com.lcb.ecommercebackend.project.model.dbSchema.OrderItemEntity;
import com.lcb.ecommercebackend.project.model.dbSchema.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItemEntity, Integer> {
    public OrderItemEntity findByOrderItemId(String orderItemId); //which orderItemId bel-ong to which order/item
    public List<OrderItemEntity> findByOrderId(Integer orderId); //which orderId bel-ong to what what orders/items
}
