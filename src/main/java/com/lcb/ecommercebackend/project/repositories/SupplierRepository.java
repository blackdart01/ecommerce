package com.lcb.ecommercebackend.project.repositories;

import com.lcb.ecommercebackend.project.model.dbSchema.SupplierEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupplierRepository extends JpaRepository<SupplierEntity, Integer> {
    public SupplierEntity findBySupplierId(String supplierId);
    public SupplierEntity findSupplierById(Integer id);
    public SupplierEntity findByUserName(String userName);
}
