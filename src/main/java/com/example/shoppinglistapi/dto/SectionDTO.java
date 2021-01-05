package com.example.shoppinglistapi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Relation(collectionRelation = "sections", itemRelation = "section")
public class SectionDTO extends RepresentationModel<SectionDTO> {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long id;
    @NonNull
    private String name;

    /*
    Links
    self
     */
}
