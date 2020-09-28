package com.example.antonapi.service.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ImageDTO {
    private Integer width;
    private Integer height;
    private byte[] image;
}
