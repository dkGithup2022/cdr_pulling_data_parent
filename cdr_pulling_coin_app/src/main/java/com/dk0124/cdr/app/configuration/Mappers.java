package com.dk0124.cdr.app.configuration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Mappers {

    @Bean
    public ModelMapper modelMapper(){
        return new CustomModelMapper();
    }

    @Bean
    public ObjectMapper objectMapper (){
        return new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    class CustomModelMapper  extends ModelMapper{
        @Override
        public <D> D map(Object source, Class<D> destinationType) {
            if(source == null){
                return null;
            }
            return super.map(source, destinationType);
        }

    }
}
