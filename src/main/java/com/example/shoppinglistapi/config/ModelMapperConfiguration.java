package com.example.shoppinglistapi.config;

import com.example.shoppinglistapi.model.Unit;
import com.example.shoppinglistapi.repository.UnitRepository;
import com.example.shoppinglistapi.dto.UnitDTO;
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
        Converter<Unit, UnitDTO> unitToUnitDTOConverter = context -> {
            Unit src = context.getSource();
            return UnitDTO.builder()
                    .id(src.getId())
                    .abbreviation(src.toString())
                    .build();
        };

        Converter<UnitDTO, Unit> unitDtoToUnitConverter = context -> {
            UnitDTO src = context.getSource();
            return unitRepository.findById(src.getId()).orElseThrow();
        };

        ModelMapper mapper =  new ModelMapper();
        mapper.addConverter(unitToUnitDTOConverter, Unit.class, UnitDTO.class);
        mapper.addConverter(unitDtoToUnitConverter, UnitDTO.class, Unit.class);
        return mapper;
    }
}
