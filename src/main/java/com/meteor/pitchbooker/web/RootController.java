package com.meteor.pitchbooker.web;

import com.meteor.pitchbooker.domain.Club;
import com.meteor.pitchbooker.domain.Pitch;
import com.meteor.pitchbooker.repository.ClubRepository;
import com.meteor.pitchbooker.repository.PitchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    @Autowired
    private ClubRepository clubRepository;

    private Logger logger = LoggerFactory.getLogger(RootController.class);
    @GetMapping("/")
    public String showWelcome(){
        return "welcome";
    }

    @GetMapping("/error")
    public String showError(){
        return "error";
    }
    @GetMapping("/pitch")
    public String showPitches(Model model){
        List<Pitch> findPitches = pitchRepository.findAll();
        model.addAttribute("pitches", findPitches);

        model.addAttribute("pitch", new Pitch());
        return "pitch";
    }
    @PostMapping("/pitch")
    public String storePitch(@ModelAttribute Pitch pitch, ModelMap model){
        pitchRepository.save(pitch);
        List<Pitch> findPitches = pitchRepository.findAll();
        model.addAttribute("pitches", findPitches);
        logger.info("New pitch created - {}", pitch.getName());

        return "pitch";
    }
    @GetMapping("/club")
    public String showClubs(Model model){
        List<Club> findClubs = clubRepository.findAll();
        model.addAttribute("clubs", findClubs);

        model.addAttribute("club", new Club());

        List<Pitch> findPitches = pitchRepository.findAll();
        model.addAttribute("pitches", findPitches);

        model.addAttribute("pitch", new Pitch());
        return "club";
    }
    @PostMapping("/club")
    public String storeClub(@ModelAttribute Club club, @ModelAttribute Pitch pitch, ModelMap model){
        clubRepository.save(club);
        logger.info("Saved Club as {}", club.getClubName());

        List<Club> findClubs = clubRepository.findAll();
        model.addAttribute("clubs", findClubs);

        List<Pitch> findPitches = pitchRepository.findAll();
        model.addAttribute("pitches", findPitches);

        logger.info("Clubs pitches are: {}", pitch.getName());
        return "club";
    }
    @GetMapping("/clubset")
    public String showClubSet(Model model){
        List<Club> findClubs = clubRepository.findAll();
        model.addAttribute("clubs", findClubs);

        model.addAttribute("club", new Club());

        List<Pitch> findPitches = pitchRepository.findAll();
        model.addAttribute("pitches", findPitches);

        model.addAttribute("pitch", new Pitch());
        return "clubset";
    }

    @Autowired
    public void setPitchRepository(PitchRepository pitchRepository){
        this.pitchRepository = pitchRepository;
    }

    @Autowired
    public void setClubRepository(ClubRepository clubRepository){
        this.clubRepository = clubRepository;
    }
}
