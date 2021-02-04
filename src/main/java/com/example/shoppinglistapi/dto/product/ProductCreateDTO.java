package com.example.shoppinglistapi.dto.product;

import lombok.*;
import org.springframework.hateoas.server.core.Relation;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Relation(itemRelation = "product")
public class ProductCreateDTO {

    @NonNull
    public String name;
    @NonNull
    public Long defaultUnitId;
    @NonNull
    public Long sectionId;
    @NonNull
    public String imageUrl;

}
