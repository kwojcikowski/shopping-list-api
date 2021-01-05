package com.example.antonapi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
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

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long id;
    @NonNull
    private String name;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String urlFriendlyName;

    /*
    Links
    self
     */
}
