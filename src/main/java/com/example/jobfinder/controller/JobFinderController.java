package com.example.jobfinder.controller;

import com.example.jobfinder.entity.UnifiedOfferEntity;
import com.example.jobfinder.forms.UserPreferencesForm;
import com.example.jobfinder.modelDTO.SeniorityDTO;
import com.example.jobfinder.modelDTO.SkillDTO;
import com.example.jobfinder.services.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/service")
public class JobFinderController {
    private final UnifiedOfferService unifiedOfferService;
    private final JobFinderService jobFinderService;
    private final EmailSenderService emailSenderService;
    private final UserService userService;
    private final SkillService skillService;
    private final SeniorityService seniorityService;
    private final HelperService helperService;

    private final MapperService mapperService;

    @GetMapping("/preferences")
    public String setPreferences(ModelMap modelMap) {
        modelMap.addAttribute("userPreferencesForm", new UserPreferencesForm());

        modelMap.addAttribute("selectableTypeOfContract", List.of("UOP", "B2B"));
        modelMap.addAttribute("selectableSkills",
                mapperService.mapSetToSetOfClass(skillService.getAll(), SkillDTO.class));
        modelMap.addAttribute("selectableSeniority",
                mapperService.mapSetToSetOfClass(seniorityService.getAll(), SeniorityDTO.class));
        return "/account/userPreferences";
    }

    @PostMapping("/submit")
    public String submitPreferences(@ModelAttribute("userPreferencesForm") @Valid UserPreferencesForm userPreferencesForm) {
        userService.saveUserPreferences(userPreferencesForm);
        jobFinderService.matchOffersAndSendToUsers();
        return "/account/userPreferencesSubmit";
    }
}
