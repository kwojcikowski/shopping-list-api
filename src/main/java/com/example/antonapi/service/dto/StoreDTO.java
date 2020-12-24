package com.example.antonapi.service.dto;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Relation(collectionRelation = "stores", itemRelation = "store")
public class StoreDTO extends RepresentationModel<StoreDTO> {

    private Long id;
    @NonNull
    private String name;
    @NonNull
    private String urlFriendlyName;

    /*
    Links
    self
     */
}
