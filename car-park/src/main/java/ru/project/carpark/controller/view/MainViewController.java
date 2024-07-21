package ru.project.carpark.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
public class MainViewController {

    @GetMapping("/login")
    public String signIn() {
        return "login";
    }

    @GetMapping("/index")
    public String getAll() {
        return "index";
    }

    @GetMapping("/index/enterprise")
    public String getEnterprise() {
        return "enterprise";
    }
}
