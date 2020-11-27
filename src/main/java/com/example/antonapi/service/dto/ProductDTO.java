package com.example.antonapi.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDTO extends RepresentationModel<ProductDTO> {
    private Long id;

    @NonNull
    private String name;
    @NonNull
    private UnitDTO defaultUnit;
    @NonNull
    private SectionDTO section;

    //Used for handling incoming image urls
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String image;

    /*
    Links
    self
    image
    thumbImage
     */
}