package com.example.shoppinglistapi.controller;

import com.example.shoppinglistapi.config.ModelMapperConfiguration;
import com.example.shoppinglistapi.model.BaseUnit;
import com.example.shoppinglistapi.model.Prefix;
import com.example.shoppinglistapi.model.Unit;
import com.example.shoppinglistapi.repository.UnitRepository;
import com.example.shoppinglistapi.service.assembler.UnitModelAssembler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = UnitModelAssembler.class)
@WebMvcTest(controllers = UnitController.class)
@Import({UnitController.class, ModelMapperConfiguration.class})
@AutoConfigureRestDocs(outputDir = "target/generated-snippets/units")
public class TestUnitController {

    @MockBean
    private UnitRepository unitRepository;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetAllUnitsSuccessful() throws Exception {
        final Prefix none = Prefix.builder()
                .id(1L)
                .name("NONE")
                .abbreviation("")
                .scale(1.0)
                .build();
        final BaseUnit baseUnitLiter = BaseUnit.builder()
                .id(1L)
                .name("LITER")
                .abbreviation("l")
                .build();
        final Unit liter = Unit.builder()
                .id(1L)
                .baseUnit(baseUnitLiter)
                .prefix(none)
                .build();
        final BaseUnit baseUnitGram = BaseUnit.builder()
                .id(2L)
                .name("GRAM")
                .abbreviation("g")
                .build();
        final Unit gram = Unit.builder()
                .id(2L)
                .baseUnit(baseUnitGram)
                .prefix(none)
                .build();
        List<Unit> units = List.of(liter, gram);
        when(unitRepository.findAll()).thenReturn(units);
        mockMvc.perform(get("/units"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$._embedded.units", hasSize(units.size())))
                .andDo(document("get-all-units",
                        preprocessResponse(prettyPrint()),
                        responseFields(
                            fieldWithPath("_embedded.units[].id").description("The id of a unit."),
                            fieldWithPath("_embedded.units[].abbreviation").description("The common abbreviation representing unit."),
                            subsectionWithPath("_embedded.units[]._links").description("Links to resources.")
                        )
                ));
    }

    @Test
    public void testGetUnitByIdSuccessful() throws Exception {
        final Prefix none = Prefix.builder()
                .id(1L)
                .name("NONE")
                .abbreviation("")
                .scale(1.0)
                .build();
        final BaseUnit baseUnitLiter = BaseUnit.builder()
                .id(1L)
                .name("LITER")
                .abbreviation("l")
                .build();
        final Unit liter = Unit.builder()
                .id(1L)
                .baseUnit(baseUnitLiter)
                .prefix(none)
                .build();
        when(unitRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(liter));
        mockMvc.perform(get("/units/{id}", 1)).andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andDo(document("get-unit-by-id",
                        preprocessResponse(prettyPrint()),
                        links(linkWithRel("self").description("Link with a self reference to a unit.")),
                        pathParameters(
                            parameterWithName("id").description("The id of unit to fetch.")
                        ),
                        responseFields(
                            fieldWithPath("id").description("The id of a unit."),
                            fieldWithPath("abbreviation").description("The common abbreviation representing unit."),
                            subsectionWithPath("_links").description("Links to resources.")
                )));
    }

    @Test
    public void testGetUnitByIdReturnNotFoundOnNonExistingUnit() throws Exception {
        when(unitRepository.findById(1L)).thenReturn(Optional.empty());
        mockMvc.perform(get("/units/1")).andExpect(status().isNotFound());
    }
}
