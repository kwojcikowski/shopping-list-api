package com.example.antonapi.config;
import com.example.antonapi.deserializer.ProductDeserializer;
import com.example.antonapi.model.Product;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = {"com.example.antonapi.repository"})
public class JpaConfiguration{

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper jsonObjectMapper = new ObjectMapper();
        SimpleModule simpleModule = new SimpleModule();

        simpleModule.setDeserializerModifier(new BeanDeserializerModifier() {
            @Override
            public JsonDeserializer<?> modifyDeserializer(DeserializationConfig config, BeanDescription beanDesc,
                                                          JsonDeserializer<?> deserializer) {
                if (Product.class.isAssignableFrom(beanDesc.getBeanClass())) {
                    return new ProductDeserializer(deserializer, beanDesc.getBeanClass());
                }
                return deserializer;
            }
        });

        jsonObjectMapper.registerModule(simpleModule);

        return jsonObjectMapper.registerModule(simpleModule);
    }
}

