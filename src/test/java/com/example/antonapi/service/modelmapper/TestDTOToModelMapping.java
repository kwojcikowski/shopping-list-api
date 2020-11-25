package com.example.antonapi.service.modelmapper;

import com.example.antonapi.config.ModelMapperConfiguration;
import com.example.antonapi.model.*;
import com.example.antonapi.repository.UnitRepository;
import com.example.antonapi.service.dto.*;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestDTOToModelMapping {

    private final ModelMapper mapper = initializeModelMapper();

    private ModelMapper initializeModelMapper() {
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

        UnitRepository mockUnitRepository = mock(UnitRepository.class);
        when(mockUnitRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(liter));
        return new ModelMapperConfiguration(mockUnitRepository).modelMapper();
    }

    @Test
    public void testSectionDTOMappingSuccessful(){
        SectionDTO dairyDTO = SectionDTO.builder()
                .id(1L)
                .name("Dairy")
                .build();
        Section expected = mapper.map(dairyDTO, Section.class);
        assertAll(() -> assertThat(expected.getId()).isEqualTo(1L),
                () -> assertThat(expected.getName()).isEqualTo("Dairy"));
    }

    @Test
    public void testSectionDTOMappingSuccessfulWithNullId(){
        SectionDTO dairyDTO = SectionDTO.builder()
                .id(null)
                .name("Dairy")
                .build();
        Section expected = mapper.map(dairyDTO, Section.class);
        assertAll(() -> assertThat(expected.getId()).isEqualTo(null),
                () -> assertThat(expected.getName()).isEqualTo("Dairy"));
    }

    @Test
    public void testStoreDTOMappingSuccessful(){
        StoreDTO nearbyStoreDTO = StoreDTO.builder()
                .id(1L)
                .name("Nearby store")
                .urlFriendlyName("nearby-store")
                .build();
        Store expected = mapper.map(nearbyStoreDTO, Store.class);
        assertAll(() -> assertThat(expected.getId()).isEqualTo(1L),
                () -> assertThat(expected.getName()).isEqualTo("Nearby store"),
                () -> assertThat(expected.getUrlFriendlyName()).isEqualTo("nearby-store"));
    }

    @Test
    public void testStoreSectionDTOMappingSuccessful(){
        StoreDTO nearbyStoreDTO = StoreDTO.builder()
                .id(1L)
                .name("Nearby store")
                .urlFriendlyName("nearby-store")
                .build();
        SectionDTO dairyDTO = SectionDTO.builder()
                .id(null)
                .name("Dairy")
                .build();
        StoreSectionDTO nearbyStoreDairyDTO = StoreSectionDTO.builder()
                .id(1L)
                .store(nearbyStoreDTO)
                .section(dairyDTO)
                .position(7)
                .build();
        StoreSection expected = mapper.map(nearbyStoreDairyDTO, StoreSection.class);
        assertAll(() -> assertThat(expected.getId()).isEqualTo(1L),
                () -> assertThat(expected.getPosition()).isEqualTo(7),
                () -> assertThat(expected.getStore()).isNotNull(),
                () -> assertThat(expected.getSection()).isNotNull());
    }

    @Test
    public void testUnitDTOMappingSuccessful(){
        UnitDTO literDTO = UnitDTO.builder()
                .id(1L)
                .abbreviation("l")
                .build();

        Unit expected = mapper.map(literDTO, Unit.class);

        Prefix expectedPrefix = Prefix.builder()
                .id(1L)
                .name("NONE")
                .abbreviation("")
                .scale(1.0)
                .build();
        BaseUnit expectedBaseUnit = BaseUnit.builder()
                .id(1L)
                .name("LITER")
                .abbreviation("l")
                .build();

        assertAll(() -> assertThat(expected.getId()).isEqualTo(1L),
                () -> assertThat(expected.getBaseUnit()).isEqualTo(expectedBaseUnit),
                () -> assertThat(expected.getPrefix()).isEqualTo(expectedPrefix),
                () -> assertThat(expected.getMasterUnit()).isNull());
    }

    @Test
    public void testProductMappingSuccessful(){
        UnitDTO literDTO = UnitDTO.builder()
                .id(1L)
                .abbreviation("l")
                .build();
        SectionDTO dairyDTO = SectionDTO.builder()
                .id(1L)
                .name("Dairy")
                .build();
        ProductDTO milkDTO = ProductDTO.builder()
                .id(1L)
                .name("Milk")
                .defaultUnit(literDTO)
                .section(dairyDTO)
                .build();
        Product expected = mapper.map(milkDTO, Product.class);
        assertAll(() -> assertThat(expected.getId()).isEqualTo(1L),
                () -> assertThat(expected.getName()).isEqualTo("Milk"),
                () -> assertThat(expected.getDefaultUnit()).isNotNull(),
                () -> assertThat(expected.getSection()).isNotNull(),
                () -> assertThat(expected.getImage()).isNull());
    }

    @Test
    public void testCartItemMappingSuccessful(){
        UnitDTO literDTO = UnitDTO.builder()
                .id(1L)
                .abbreviation("l")
                .build();
        SectionDTO dairyDTO = SectionDTO.builder()
                .id(1L)
                .name("Dairy")
                .build();
        ProductDTO milkDTO = ProductDTO.builder()
                .id(1L)
                .name("Milk")
                .defaultUnit(literDTO)
                .section(dairyDTO)
                .build();
        CartItemDTO milkCartItem = CartItemDTO
                .builder()
                .id(1L)
                .product(milkDTO)
                .unit(literDTO)
                .quantity(new BigDecimal("2.5"))
                .build();

        CartItem expected = mapper.map(milkCartItem, CartItem.class);
        // Testing only cart item DTO fields, as relations as ProductDTO are tested within ProductDTOModelAssembler
        assertAll(() -> assertThat(expected.getId()).isEqualTo(1L),
                () -> assertThat(expected.getQuantity().stripTrailingZeros()).isEqualTo(new BigDecimal("2.5").stripTrailingZeros()),
                () -> assertThat(expected.getProduct()).isNotNull(),
                () -> assertThat(expected.getUnit()).isNotNull());
    }
}
