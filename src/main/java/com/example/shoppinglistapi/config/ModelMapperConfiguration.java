package com.example.shoppinglistapi.config;

import com.example.shoppinglistapi.model.Unit;
import com.example.shoppinglistapi.repository.UnitRepository;
import com.example.shoppinglistapi.dto.unit.UnitReadDto;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class ModelMapperConfiguration {

    private final @NonNull UnitRepository unitRepository;

    @Bean
    public ModelMapper modelMapper(){
        Converter<Unit, UnitReadDto> unitToUnitDTOConverter = context -> {
            Unit src = context.getSource();
            return UnitReadDto.builder()
                    .id(src.getId())
                    .abbreviation(src.toString())
                    .build();
        };

        Converter<UnitReadDto, Unit> unitDtoToUnitConverter = context -> {
            UnitReadDto src = context.getSource();
            return unitRepository.findById(src.getId()).orElseThrow();
        };

        ModelMapper mapper =  new ModelMapper();
        mapper.addConverter(unitToUnitDTOConverter, Unit.class, UnitReadDto.class);
        mapper.addConverter(unitDtoToUnitConverter, UnitReadDto.class, Unit.class);
        return mapper;
    }
}
