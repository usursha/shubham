package com.hpy.ops360.AssetService.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssetMediaTypeEntity {

    @Id
    @GeneratedValue(strategy=GenerationType.UUID)
    @JsonIgnore
    private String assetId;
//    @NotEmpty(message = "atmId cannot be null!!")
    private String atmId;
//    @NotEmpty(message = "parentId cannot be null!!")
    private String parentId;
//    @NotEmpty(message = "parentType cannot be null!!")
    private String parentType;
//    @NotEmpty(message = "mediaType cannot be null!!")
    private String mediaType;
    @JsonIgnore
    private String docPath;
//    @NotEmpty(message = "fileName cannot be null!!")
    private String fileName;   
    @Transient
    private String base64String;
    
    

}


