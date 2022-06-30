package com.example.jobfinder.services;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MapperService {
    private final ModelMapper mapper;

    public <S, T> Set<T> mapSetToSetOfClass(Set<S> source, Class<T> targetClass) {
        return source
                .stream()
                .map(element -> mapper.map(element, targetClass))
                .collect(Collectors.toSet());
    }

    public <S, T> T mapObjectToOtherObject(S source, Class<T> targetClass) {
        return mapper.map(source, targetClass);
    }

}
