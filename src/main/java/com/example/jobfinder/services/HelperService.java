package com.example.jobfinder.services;

import com.example.jobfinder.entity.UserEntity;
import com.example.jobfinder.enums.Seniority;
import com.example.jobfinder.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class HelperService {
    private final UserEntityRepository userEntityRepository;

    public UserEntity getCurrentLoggedUser(){
        return userEntityRepository.findByLogin("rsuski7");
    }

    public boolean checkIfStringCanBeEnum(String string) {
        return Stream.of(Seniority.values())
                .map(Seniority::name)
                .collect(Collectors.toList())
                .contains(string.toUpperCase());
    }
}
