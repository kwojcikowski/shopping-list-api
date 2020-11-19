package com.example.antonapi.service.dto;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SectionDTO extends RepresentationModel<SectionDTO> {

    private Long id;
    @NonNull
    private String name;

    /*
    Links
    self
     */
}
