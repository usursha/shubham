package com.hpy.ops360.ticketing.cm.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageSaveResponse {
    private List<String> imageUrls;
}
