package com.example.shoppinglistapi.dto.storesection;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreSectionCreateDto {

    @NonNull
    public Long id;
    @NonNull
    public Integer position;

}
