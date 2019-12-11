package org.altstu.otp.cellular.network.rest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {
    @GetMapping("/")
    public String main(Model model) {
        return "welcome"; //view
    }
}
