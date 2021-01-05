package com.example.shoppinglistapi.config;
import com.example.shoppinglistapi.repository.UnitRepository;
import com.example.shoppinglistapi.service.tools.SmartUnits;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.PostConstruct;

@Configuration
@EnableJpaRepositories(basePackages = {"com.example.shoppinglistapi.repository"})
@RequiredArgsConstructor
public class MainConfiguration {

    final @NonNull ApplicationContext applicationContext;

    @PostConstruct
    public void initializeSmartUnits(){
        SmartUnits.unitRepository = applicationContext.getBean("unitRepository", UnitRepository.class);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedMethods("HEAD", "GET", "PUT", "POST", "DELETE", "PATCH");
            }
        };
    }
}

