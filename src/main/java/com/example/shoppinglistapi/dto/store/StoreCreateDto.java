package com.example.shoppinglistapi.dto.store;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreCreateDto {

    @NonNull
    public String name;

    /*
    Links
    self
     */
}
