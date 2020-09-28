package com.example.antonapi.service.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

@EqualsAndHashCode(callSuper = true)
@Data
public class SectionDTO extends RepresentationModel<SectionDTO> {
    private Long id;
    private String name;

    /*
    Links
    self
     */
}
