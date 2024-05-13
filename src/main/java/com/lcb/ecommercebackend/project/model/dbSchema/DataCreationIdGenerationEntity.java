package com.lcb.ecommercebackend.project.model.dbSchema;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

import java.time.ZonedDateTime;

@Entity
@Data
@Table(name = "id_generation")
public class DataCreationIdGenerationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    @Column(name="id")
    private Integer id;

    @JsonProperty("jrnlNum")
    @Column(name="jrnlnum")
    private String jrnlNum;

    @JsonProperty("api")
    @Column(name="api")
    private String apiName;

    @JsonProperty("idSaved")
    @Column(name="id_saved")
    private String idSaved;

    @JsonProperty("requestMethod")
    @Column(name="request_method")
    private String requestMethod;

    @JsonProperty("created_at")
    @Column(name="created_at", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private ZonedDateTime createdAt;
}
