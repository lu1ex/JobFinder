package com.example.jobfinder.controller;

import com.example.jobfinder.forms.RegisterForm;
import com.example.jobfinder.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/account")
public class AccountContoller {
    private final UserService userService;

    @GetMapping("/register")
    public String register(ModelMap modelMap) {
        modelMap.addAttribute("registerForm", new RegisterForm());
        return "/account/userRegister";
    }

    @PostMapping("/register/execute")
    public String registerExecute(ModelMap modelMap, @ModelAttribute("registerForm") @Valid RegisterForm registerForm,
                                  BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            modelMap.addAttribute("errors", bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.joining("\n")));
            return "/account/userRegister";
        }
        userService.addUser(registerForm);

        return "/account/userRegister_result";
    }

    @RequestMapping("/login")
    public String login() {
        return "login";
    }

   /* @RequestMapping("/logout")
    public String logoutUser() {
        SecurityContextHolder.getContext().setAuthentication(null);
        return "logout";
    }*/

}
