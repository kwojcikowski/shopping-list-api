package com.example.antonapi.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

@EqualsAndHashCode(callSuper = true)
@Data
public class ProductDTO extends RepresentationModel<ProductDTO> {
    private Long id;
    private String name;
    private UnitDTO defaultUnit;
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
