package com.aswe.communicraft.mapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

    /**
     * This a generic class to map between models and dto using model mapper
     *
     * @param <T1> This is a type of provided object
     * @param <T2> This is a class of output object required
     */
@Component
@RequiredArgsConstructor
public class Mapper<T1, T2> {

    private final ModelMapper modelMapper;

    public T2 toDto(T1 model, Class<T2> dtoClass){
        return modelMapper.map(model, dtoClass);
    }

    public T1 toEntity(T2 dto, Class<T1> modelClass){
        return modelMapper.map(dto, modelClass);
    }
}