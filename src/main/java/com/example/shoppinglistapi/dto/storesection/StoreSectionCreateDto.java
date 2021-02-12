package com.example.shoppinglistapi.dto.storesection;

import lombok.*;

import javax.validation.constraints.Min;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreSectionCreateDto {

    @Min(1)
    public Long storeId;
    @Min(1)
    public Long sectionId;
    @Min(1)
    public Integer position;

}
