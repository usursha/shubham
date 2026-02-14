package com.hpy.ops360.AssetService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageData {
    private String fileName;       // e.g., "atm_front.jpg"
    private String base64Content; // base64 string of the image

}
