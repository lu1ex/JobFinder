package com.example.jobfinder.services;

import com.example.jobfinder.enums.Seniority;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class HelperService {

    public boolean checkIfStringCanBeEnum(String string) {
        return Stream.of(Seniority.values())
                .map(Seniority::name)
                .collect(Collectors.toList())
                .contains(string.toUpperCase());
    }
}
