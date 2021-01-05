package com.example.shoppinglistapi;

import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestModelMapperConfiguration {

    @Bean
    ModelMapper modelMapper(){
        return new ModelMapper();
    }
}
