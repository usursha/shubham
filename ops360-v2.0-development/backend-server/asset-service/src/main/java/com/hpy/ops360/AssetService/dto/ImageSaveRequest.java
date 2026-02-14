package com.hpy.ops360.AssetService.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageSaveRequest {
    private String atmId;
    private String ticketNo;
    private List<ImageData> images;

}
