package com.fa.ims.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController extends BaseController {

    @GetMapping(value = {"/", "/home"})
    public String homePage() {
        return "home/home";
    }
}
