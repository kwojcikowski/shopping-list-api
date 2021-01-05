package com.example.antonapi.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageDTO {
    @NonNull
    private Integer width;
    @NonNull
    private Integer height;
    @NonNull
    private byte[] image;
}
