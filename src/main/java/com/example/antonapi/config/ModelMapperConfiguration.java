package com.example.antonapi.config;

import com.example.antonapi.model.Unit;
import com.example.antonapi.service.dto.UnitDTO;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfiguration {

    @Bean
    public ModelMapper modelMapper(){
        Converter<Unit, UnitDTO> unitToUnitDTOConverter = context -> {
            Unit src = context.getSource();
            return UnitDTO.builder()
                    .id(src.getId())
                    .abbreviation(src.toString())
                    .build();
        };
        ModelMapper mapper =  new ModelMapper();
        mapper.addConverter(unitToUnitDTOConverter, Unit.class, UnitDTO.class);
        return mapper;
    }
}
