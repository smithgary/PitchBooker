package com.meteor.pitchbooker;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class RootController {

    private final PitchRepository pitchRepository;

    @GetMapping("/pitch")
    public String indexMap(Model model){
        model.addAttribute("pitch", new Pitch());
        return "pitch";
    }
    @PostMapping("/pitch")
    public String storePitch(@ModelAttribute Pitch pitch){
        //Store?
        System.out.println("New pitch: " + pitch.getName());
        return "pitch";
    }
}
