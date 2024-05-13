package com.lcb.ecommercebackend.project.services;

import com.lcb.ecommercebackend.project.model.dbSchema.ProductCategoryEntity;
import com.lcb.ecommercebackend.project.repositories.ProductCategoryRepository;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

@Component
public class PredefinedEntryListener  {
    @Autowired
    private ProductCategoryRepository repository;

    @Value("${predefined.category.name}")
    private String predefinedName;

    @PostConstruct
    public void createPredefinedCategory() {
        System.out.println("hell " + repository.countByPredefinedName(predefinedName));
        if (repository.countByPredefinedName(predefinedName) == 0)
        {
            ProductCategoryEntity category = new ProductCategoryEntity();
            category.setCategoryName("Other Category");
            category.setCategoryId("CATOTDEFEC");
            category.setCategoryDescription("Other Category Description");
            category.setIsActive(true);
            category.setCreatedAt(ZonedDateTime.now());
            repository.save(category);
        }
    }
}