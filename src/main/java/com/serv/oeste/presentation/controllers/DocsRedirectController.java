package com.serv.oeste.presentation.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DocsRedirectController {
    @GetMapping({"/swagger", "/docs"})
    public String redirectToDocs() {
        return "redirect:/scalar";
    }
}
