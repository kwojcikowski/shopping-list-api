package com.example.antonapi.config;
import com.example.antonapi.model.CartItem;
import com.example.antonapi.model.Product;
import com.example.antonapi.model.Section;
import com.example.antonapi.model.Unit;
import com.example.antonapi.service.deserializer.CartItemDeserializer;
import com.example.antonapi.service.deserializer.ProductDeserializer;
import com.example.antonapi.service.deserializer.SectionDeserializer;
import com.example.antonapi.service.deserializer.UnitDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableJpaRepositories(basePackages = {"com.example.antonapi.repository"})
public class AntonConfiguration {

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

    @Bean
    public Jackson2ObjectMapperBuilder objectMapperBuilder() {
        SimpleModule m = new SimpleModule();
        m.addDeserializer(Product.class, new ProductDeserializer());
        m.addDeserializer(Unit.class, new UnitDeserializer());
        m.addDeserializer(CartItem.class, new CartItemDeserializer());
        m.addDeserializer(Section.class, new SectionDeserializer());
        return new Jackson2ObjectMapperBuilder().modules(m);
    }
}

