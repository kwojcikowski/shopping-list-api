package com.example.antonapi.controller;

import com.example.antonapi.config.ModelMapperConfiguration;
import com.example.antonapi.model.BaseUnit;
import com.example.antonapi.model.Prefix;
import com.example.antonapi.model.Unit;
import com.example.antonapi.repository.UnitRepository;
import com.example.antonapi.service.assembler.UnitModelAssembler;
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

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = UnitModelAssembler.class)
@WebMvcTest(controllers = UnitController.class)
@Import({UnitController.class, ModelMapperConfiguration.class})
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
                .andExpect(jsonPath("$._embedded.units", hasSize(units.size())));
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
        mockMvc.perform(get("/units/1")).andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$._links.self.href", endsWith("/units/1")));
    }

    @Test
    public void testGetUnitByIdReturnNotFoundOnNonExistingUnit() throws Exception {
        when(unitRepository.findById(1L)).thenReturn(Optional.empty());
        mockMvc.perform(get("/units/1")).andExpect(status().isNotFound());
    }
}
