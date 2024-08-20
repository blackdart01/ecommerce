package com.lcb.ecommercebackend.project.repositories;

import com.lcb.ecommercebackend.project.model.dbSchema.SellerAuthenticationEntity;
import com.lcb.ecommercebackend.project.model.dbSchema.SupplierEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SellerAuthenticationRepository extends JpaRepository<SellerAuthenticationEntity, Integer> {
    public SellerAuthenticationEntity findBySupplierId(String supplierId);
    public SellerAuthenticationEntity findByUserName(String userName);
    public SellerAuthenticationEntity findByContactEmail(String contactEmail);
    public SellerAuthenticationEntity findByContactPhone(String contactPhone);
}
