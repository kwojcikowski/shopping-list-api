package com.example.antonapi.service.dto;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
