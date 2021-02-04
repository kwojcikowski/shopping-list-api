package com.example.shoppinglistapi.dto.storesection;

import com.example.shoppinglistapi.dto.store.StoreReadDto;
import com.example.shoppinglistapi.dto.section.SectionReadDto;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Relation(itemRelation = "storeSection", collectionRelation = "storeSections")
public class StoreSectionReadDto extends RepresentationModel<StoreSectionReadDto> {

    @NonNull
    public Long id;
    @NonNull
    public Integer position;
    @NonNull
    public StoreReadDto store;
    @NonNull
    public SectionReadDto section;

    /*
    Links
    self
     */
}
