package com.example.shoppinglistapi.dto.section;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SectionCreateDto {

    @NonNull
    public String name;

}
