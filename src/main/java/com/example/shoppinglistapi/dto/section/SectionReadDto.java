package com.example.shoppinglistapi.dto.section;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Relation(collectionRelation = "sections", itemRelation = "section")
public class SectionReadDto extends RepresentationModel<SectionReadDto> {

    @NonNull
    public Long id;
    @NonNull
    public String name;

    /*
    Links
    self
     */
}
