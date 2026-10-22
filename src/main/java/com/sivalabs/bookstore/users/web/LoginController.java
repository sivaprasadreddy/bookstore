package com.sivalabs.bookstore.users.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
class LoginController {
    @GetMapping("/login")
    public String login() {
        return "users/login";
    }
}
