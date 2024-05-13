package com.lcb.ecommercebackend.project.repositories;

import com.lcb.ecommercebackend.project.model.dbSchema.ProductCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategoryEntity, Integer> {
    public ProductCategoryEntity findByCategoryId(String categoryId);
    @Query("SELECT COUNT(p) FROM ProductCategoryEntity p WHERE p.categoryId  = :predefinedName")
    int countByPredefinedName(@Param("predefinedName") String predefinedName);

//    int countByPredefinedName(@Param("predefinedName") String predefinedName);

}
