package com.example.antonapi.controller;

import com.example.antonapi.model.Unit;
import com.example.antonapi.repository.UnitRepository;
import com.example.antonapi.service.assembler.UnitModelAssembler;
import com.example.antonapi.service.tools.FilePath;
import com.example.antonapi.service.tools.JsonFileService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = UnitModelAssembler.class)
@WebMvcTest(controllers = UnitController.class)
@Import(UnitController.class)
public class TestUnitController {

    @MockBean
    private UnitRepository unitRepository;

    @Autowired
    private MockMvc mockMvc;

    private final List<Unit> testUnits;

    public TestUnitController() throws IOException {
        this.testUnits = JsonFileService.loadCollectionFromFile(FilePath.UNITS_FILE_PATH, Unit.class);
    }

    @Test
    public void testSuccessfulGetAllUnits() throws Exception {
        when(unitRepository.findAll()).thenReturn(testUnits);
        mockMvc.perform(get("/units"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$._embedded.units", hasSize(testUnits.size())));
//                .andExpect(jsonPath("$._embedded.units", ));
    }

    @Test
    public void testSuccessfulGetUnitById() throws Exception {
        when(unitRepository.getOne(testUnits.get(0).getId())).thenReturn(testUnits.get(0));
        mockMvc.perform(get("/units/"+testUnits.get(0).getId())).andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$", hasSize(1)));
    }
}
