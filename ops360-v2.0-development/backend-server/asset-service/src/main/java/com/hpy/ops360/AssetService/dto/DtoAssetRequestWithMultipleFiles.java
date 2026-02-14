package com.hpy.ops360.AssetService.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DtoAssetRequestWithMultipleFiles {
	
	@JsonIgnore
    private String assetId;
	@NotEmpty(message = "atmId cannot be null!!")
    private String atmId;
	@NotEmpty(message = "parentId cannot be null!!")
    private String parentId;
	@NotEmpty(message = "parentType cannot be null!!")
    private String parentType;
	@NotEmpty(message = "mediaType cannot be null!!")
    private String mediaType;
    @JsonIgnore
    private String docPath;
    @NotNull
    @Valid
    private List<MediaFilesDto> mediafiles; 

}

