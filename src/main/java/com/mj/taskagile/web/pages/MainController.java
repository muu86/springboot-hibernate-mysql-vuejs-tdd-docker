package com.mj.taskagile.web.pages;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    
    @GetMapping({ "/", "/login", "/register" })
    public String entry() {
        return "index";
    }
}
