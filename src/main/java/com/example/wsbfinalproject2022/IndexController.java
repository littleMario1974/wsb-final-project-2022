package com.example.wsbfinalproject2022;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @GetMapping("/")
    public String index() {

        return "index";
    }

    @GetMapping("/contact")
    public String contact() {

        return "contact";
    }
}
