package com.example.shoppinglistapi.dto.product;

import lombok.*;
import org.springframework.hateoas.server.core.Relation;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Relation(itemRelation = "product")
public class ProductCreateDto {

    @NotBlank
    public String name;
    @Min(1)
    public Long defaultUnitId;
    @Min(1)
    public Long sectionId;
    @Pattern(regexp = "[a-zA-Z:/\\-1-9]+\\.(jpg|png)", message = "Image url has to end with .png or .jpg")
    public String imageUrl;

}
