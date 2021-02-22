package com.example.shoppinglistapi.dto.unit;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Relation(collectionRelation = "units", itemRelation = "unit")
public class UnitReadDto extends RepresentationModel<UnitReadDto> {

    @NonNull
    public String abbreviation;

    /*
    Links
    self
     */
}
