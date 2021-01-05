package com.example.antonapi.dto;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Relation(collectionRelation = "units", itemRelation = "unit")
public class UnitDTO extends RepresentationModel<UnitDTO> {
    private Long id;
    @NonNull
    private String abbreviation;

    /*
    Links
    self
     */
}
