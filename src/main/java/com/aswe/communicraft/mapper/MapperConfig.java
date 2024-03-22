package com.aswe.communicraft.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

    /**
     * Declared model mapper as a bean to inject this bean when required
     */
@Component
public class MapperConfig {
    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }
}