package com.donfaq.ruchi.integration.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/")
    public String index() {return "about";}

    @GetMapping("/bot")
    public String bot() {return "bot";}

    @GetMapping("/about")
    public String about() {return "about";}

    @GetMapping("/games")
    public String games() {return "games";}
}
