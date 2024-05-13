package com.lcb.ecommercebackend.project.repositories;

import com.lcb.ecommercebackend.project.model.dbSchema.DataCreationIdGenerationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DataCreationIdGenerationRepository extends JpaRepository<DataCreationIdGenerationEntity, Integer> {
    public String findByApiName(String apiName);

}
