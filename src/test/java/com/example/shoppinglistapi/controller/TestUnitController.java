package com.example.shoppinglistapi.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureRestDocs(outputDir = "target/generated-snippets/units")
@SpringBootTest
@AutoConfigureMockMvc
public class TestUnitController {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetAllUnitsSuccessful() throws Exception {
        mockMvc.perform(get("/units"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andDo(document("get-all-units",
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("_embedded.units[].abbreviation").description("The common abbreviation representing unit."),
                                subsectionWithPath("_embedded.units[]._links").description("Links to resources.")
                        )
                ));
    }

    @Test
    public void testGetUnitByAbbreviationSuccessful() throws Exception {
        mockMvc.perform(get("/units/{abbreviation}", "g")).andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andDo(document("get-unit-by-abbreviation",
                        preprocessResponse(prettyPrint()),
                        links(linkWithRel("self").description("Link with a self reference to a unit.")),
                        pathParameters(
                                parameterWithName("abbreviation").description("The abbreviation of unit to fetch.")
                        ),
                        responseFields(
                                fieldWithPath("abbreviation").description("The common abbreviation representing unit."),
                                subsectionWithPath("_links").description("Links to resources.")
                        )));
    }

    @Test
    public void testGetUnitByAbbreviationReturnNotFoundOnNonExistingUnit() throws Exception {
        mockMvc.perform(get("/units/{abbreviation}", "wrong-abbreviation"))
                .andExpect(status().isNotFound());
    }
}
