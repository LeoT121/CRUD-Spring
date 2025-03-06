package com.Leonardo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/view")
public class CommonController {

    @GetMapping("/my-profile")
    public String home() {
        return "my-profile";
    }
}
