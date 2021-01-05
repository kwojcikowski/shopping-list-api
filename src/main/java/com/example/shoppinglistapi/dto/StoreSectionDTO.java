package com.example.shoppinglistapi.dto;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Relation(itemRelation = "storeSection", collectionRelation = "storeSections")
public class StoreSectionDTO extends RepresentationModel<StoreSectionDTO> {
    private Long id;
    @NonNull
    private Integer position;
    @NonNull
    private StoreDTO store;
    @NonNull
    private SectionDTO section;

    /*
    Links
    self
     */
}
