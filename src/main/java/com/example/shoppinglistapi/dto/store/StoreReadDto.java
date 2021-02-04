package com.example.shoppinglistapi.dto.store;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Relation(collectionRelation = "stores", itemRelation = "store")
public class StoreReadDto extends RepresentationModel<StoreReadDto> {

    @NonNull
    public Long id;
    @NonNull
    public String name;
    @NonNull
    public String urlFriendlyName;

    /*
    Links
    self
     */
}
