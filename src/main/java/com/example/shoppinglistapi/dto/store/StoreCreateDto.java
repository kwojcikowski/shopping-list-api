package com.example.shoppinglistapi.dto.store;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreCreateDto {

    @NotBlank
    public String name;

    /*
    Links
    self
     */
}
