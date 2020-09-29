package com.example.antonapi.service.dto;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
public class UnitDTO extends RepresentationModel<UnitDTO> {
    private Long id;
    private String abbreviation;

    /*
    Links
    self
     */
}
