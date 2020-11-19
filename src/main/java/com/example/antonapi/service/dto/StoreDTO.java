package com.example.antonapi.service.dto;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
