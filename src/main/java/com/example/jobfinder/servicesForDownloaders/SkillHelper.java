package com.example.jobfinder.servicesForDownloaders;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Data
@RequiredArgsConstructor
public class SkillHelper {
    private List<String> skillsThatHaveAlreadyOccurred = new ArrayList<>();
}
