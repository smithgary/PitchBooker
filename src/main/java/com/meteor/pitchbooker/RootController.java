package com.meteor.pitchbooker;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class RootController {

    @GetMapping("/pitch")
    public String indexMap(Model model){
        model.addAttribute("pitch", new Pitch());
        return "pitch";
    }
}
