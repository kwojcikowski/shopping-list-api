package com.example.antonapi.service.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

@EqualsAndHashCode(callSuper = true)
@Data
public class UnitDTO extends RepresentationModel<StoreSectionDTO> {
    private Long id;
    private String abbreviation;

    /*
    Links
    self
     */
}
