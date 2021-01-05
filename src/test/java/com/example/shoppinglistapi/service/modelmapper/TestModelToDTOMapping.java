package com.example.shoppinglistapi.service.modelmapper;

import com.example.shoppinglistapi.config.ModelMapperConfiguration;
import com.example.shoppinglistapi.model.*;
import com.example.shoppinglistapi.repository.UnitRepository;
import com.example.shoppinglistapi.dto.*;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.mock;

public class TestModelToDTOMapping {

    ModelMapper modelMapper = initializeModelMapper();

    private ModelMapper initializeModelMapper() {
        UnitRepository mockUnitRepository = mock(UnitRepository.class);
        return new ModelMapperConfiguration(mockUnitRepository).modelMapper();
    }

    @Test
    public void testSectionMappingSuccessful(){
        Section actual = Section.builder()
                .id(1L)
                .name("Dairy")
                .build();

        SectionDTO expected = modelMapper.map(actual, SectionDTO.class);

        assertAll(() -> assertThat(expected.getId()).isEqualTo(1L),
                () -> assertThat(expected.getName()).isEqualTo("Dairy"));
    }

    @Test
    public void testStoreMappingSuccessful(){
        Store actual = Store.builder()
                .id(1L)
                .name("Close store")
                .urlFriendlyName("close-store")
                .build();
        StoreDTO expected = modelMapper.map(actual, StoreDTO.class);

        assertAll(() -> assertThat(expected.getId()).isEqualTo(1L),
                () -> assertThat(expected.getName()).isEqualTo("Close store"),
                () -> assertThat(expected.getUrlFriendlyName()).isEqualTo("close-store"));
    }

    @Test
    public void testStoreSectionMappingSuccessful(){
        Section dairy = Section.builder()
                .id(1L)
                .name("Dairy")
                .build();
        Store store = Store.builder()
                .id(1L)
                .name("Close store")
                .urlFriendlyName("close-store")
                .build();
        StoreSection actual = StoreSection.builder()
                .id(1L)
                .store(store)
                .section(dairy)
                .position(7)
                .build();

        StoreSectionDTO expected = modelMapper.map(actual, StoreSectionDTO.class);

        assertAll(() -> assertThat(expected.getId()).isEqualTo(1L),
                () -> assertThat(expected.getPosition()).isEqualTo(7),
                () -> assertThat(expected.getStore()).isNotNull(),
                () -> assertThat(expected.getSection()).isNotNull());
    }

    @Test
    public void testUnitMappingSuccessful(){
        Prefix none = Prefix.builder()
                .id(1L)
                .name("NONE")
                .abbreviation("")
                .scale(1.0)
                .build();
        BaseUnit baseUnitLiter = BaseUnit.builder()
                .id(1L)
                .name("LITER")
                .abbreviation("l")
                .build();
        Unit actual = Unit.builder()
                .id(1L)
                .baseUnit(baseUnitLiter)
                .prefix(none)
                .build();

        UnitDTO expected = modelMapper.map(actual, UnitDTO.class);

        assertAll(() -> assertThat(expected.getId()).isEqualTo(1L),
                () -> assertThat(expected.getAbbreviation()).isEqualTo("l"));
    }

    @Test
    public void testProductMappingSuccessful(){
        Prefix none = Prefix.builder()
                .id(1L)
                .name("NONE")
                .abbreviation("")
                .scale(1.0)
                .build();
        BaseUnit baseUnitLiter = BaseUnit.builder()
                .id(1L)
                .name("LITER")
                .abbreviation("l")
                .build();
        Unit liters = Unit.builder()
                .id(1L)
                .baseUnit(baseUnitLiter)
                .prefix(none)
                .build();
        Section dairy = Section.builder()
                .id(1L)
                .name("Dairy")
                .build();
        Product actual = Product.builder()
                .id(1L)
                .name("Milk")
                .defaultUnit(liters)
                .section(dairy)
                .build();

        ProductDTO expected = modelMapper.map(actual, ProductDTO.class);

        assertAll(() -> assertThat(expected.getId()).isEqualTo(1L),
                () -> assertThat(expected.getName()).isEqualTo("Milk"),
                () -> assertThat(expected.getDefaultUnit()).isNotNull(),
                () -> assertThat(expected.getSection()).isNotNull(),
                () -> assertThat(expected.getImageUrl()).isNull());
    }

    @Test
    public void testCartItemMappingSuccessful(){
        Section dairy = Section.builder()
                .id(1L)
                .name("Dairy")
                .build();
        Prefix none = Prefix.builder()
                .id(1L)
                .name("NONE")
                .abbreviation("")
                .scale(1.0)
                .build();
        BaseUnit baseUnitLiter = BaseUnit.builder()
                .id(1L)
                .name("LITER")
                .abbreviation("l")
                .build();
        Unit liter = Unit.builder()
                .id(1L)
                .baseUnit(baseUnitLiter)
                .prefix(none)
                .build();
        Product milk = Product.builder()
                .id(1L)
                .name("Milk")
                .defaultUnit(liter)
                .section(dairy)
                .build();
        CartItem actual = CartItem
                .builder()
                .id(1L)
                .product(milk)
                .unit(liter)
                .quantity(new BigDecimal("2.5"))
                .build();

        CartItemDTO expected = modelMapper.map(actual, CartItemDTO.class);

        // Testing only cart item DTO fields, as relations as ProductDTO are tested within ProductDTOModelAssembler
        assertAll(() -> assertThat(expected.getId()).isEqualTo(1L),
                () -> assertThat(expected.getQuantity().stripTrailingZeros()).isEqualTo(new BigDecimal("2.5").stripTrailingZeros()),
                () -> assertThat(expected.getProduct()).isNotNull(),
                () -> assertThat(expected.getUnit()).isNotNull());
    }
}
