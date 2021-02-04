package com.example.shoppinglistapi.dto.product;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageReadDto {

    @NonNull
    public Integer width;
    @NonNull
    public Integer height;
    @NonNull
    public byte[] image;

}
