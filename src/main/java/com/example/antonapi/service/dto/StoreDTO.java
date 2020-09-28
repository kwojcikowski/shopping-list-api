package com.example.antonapi.service.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

@EqualsAndHashCode(callSuper = true)
@Data
public class StoreDTO extends RepresentationModel<StoreDTO> {
    private Long id;
    private String name;
    private String urlFriendlyName;

    /*
    Links
    self
     */
}
