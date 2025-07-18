package com.sivalabs.bookstore.users.web;

import com.sivalabs.bookstore.users.core.RegistrationRequest;
import com.sivalabs.bookstore.users.core.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
class RegistrationController {
    private final UserService userService;

    RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/registration")
    String registrationForm(Model model) {
        model.addAttribute("user", new RegistrationRequest("", "", ""));
        return "users/registration";
    }

    @PostMapping("/registration")
    String registerUser(
            @ModelAttribute("user") @Valid RegistrationRequest registrationRequest,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "users/registration";
        }
        try {
            userService.createUser(registrationRequest);
            redirectAttributes.addFlashAttribute("message", "Registration successful");
            return "redirect:/registration/success";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/registration";
        }
    }

    @GetMapping("/registration/success")
    String registrationSuccess() {
        return "users/registration-status";
    }
}
