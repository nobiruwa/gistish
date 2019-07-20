package com.example.vuespringexample;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VueController {
    @GetMapping("/view")
    public String index(Model model) {
        model.addAttribute("eventName", "FIFA 2019");
        // templates/index.htmlを使う、という意味か
        return "index";
    }
}
