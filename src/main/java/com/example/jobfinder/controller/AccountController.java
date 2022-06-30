package com.example.jobfinder.controller;

import com.example.jobfinder.entity.UserEntity;
import com.example.jobfinder.forms.LoginForm;
import com.example.jobfinder.forms.RegisterForm;
import com.example.jobfinder.repository.UserEntityRepository;
import com.example.jobfinder.services.EmailSenderService;
import com.example.jobfinder.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class AccountController {
    private final UserService userService;
    private final EmailSenderService emailSenderService;

    @GetMapping("/register")
    public String register(ModelMap modelMap) {
        modelMap.addAttribute("registerForm", new RegisterForm());
        return "/account/register";
    }

    @PostMapping("/register/execute")
    public String registerExecute(ModelMap modelMap, @ModelAttribute("registerForm") @Valid RegisterForm registerForm,
                                  BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            modelMap.addAttribute("errors", bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.joining("\n")));
            return "/account/register";
        }
        userService.addUser(registerForm);
        UserEntity newUser = userService.getEntityByEmail(registerForm.getEmail());
        emailSenderService.sendEmailWithActivationLink(newUser.getEmail(), newUser.getLogin(), newUser.getId());
        return "/account/register_result";
    }

    @GetMapping("/{userID}/activate")
    public String activateAccount(@PathVariable String userID) {
        userService.activateUserAccount(userID);
        return "success";
    }

    @RequestMapping("/login") // TODO: DODAĆ SPRAWDZENIE CZY JEST AKTYWNY USER
    public String login(ModelMap modelMap) {
        modelMap.addAttribute("loginForm", new LoginForm());
        return "/account/login";
    }

   /* @RequestMapping("/logout")
    public String logoutUser() {
        SecurityContextHolder.getContext().setAuthentication(null);
        return "logout";
    }*/

}
