package com.meteor.pitchbooker;

import com.meteor.pitchbooker.repository.PitchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class RootController {
    @Autowired
    private PitchRepository pitchRepository;

    @GetMapping("/pitch")
    public String indexMap(Model model){
        List<Pitch> findPitches = pitchRepository.findAll();
        model.addAttribute("pitches", findPitches);

        model.addAttribute("pitch", new Pitch());
        return "pitch";
    }
    @PostMapping("/pitch")
    public String storePitch(@ModelAttribute Pitch pitch, ModelMap model){
        //Store?
        pitchRepository.save(pitch);
        List<Pitch> findPitches = pitchRepository.findAll();
        model.addAttribute("pitches", findPitches);

        System.out.println("New pitch: " + pitch.getName());
        return "pitch";
    }
}
