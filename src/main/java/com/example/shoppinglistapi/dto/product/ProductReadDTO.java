package com.example.shoppinglistapi.dto.product;

import com.example.shoppinglistapi.dto.section.SectionReadDto;
import com.example.shoppinglistapi.dto.unit.UnitReadDto;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Relation(collectionRelation = "products", itemRelation = "product")
public class ProductReadDTO extends RepresentationModel<ProductReadDTO> {

    @NonNull
    public Long id;
    @NonNull
    public String name;
    @NonNull
    public UnitReadDto defaultUnit;
    @NonNull
    public SectionReadDto section;

    /*
    Links
    self
    image
    thumbImage
     */
}
