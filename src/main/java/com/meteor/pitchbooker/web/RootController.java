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
import java.util.Optional;

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

    @PostMapping("/addPitch")
    public String allocatePitch(@ModelAttribute Pitch pitch, Club club, ModelMap model, String activeClubName, String activeClubId){
        //First handle the Choice of Club
        logger.info("Club chosen id is : " + club.getId());
        Long clubId = club.getId();
        Optional<Club> chosenClub = clubRepository.findById(clubId);
        logger.info("Pitch chosen id is : " + pitch.getId());
        Long pitchId = pitch.getId();
        Optional<Pitch> chosenPitch = pitchRepository.findById(pitchId);

        if (chosenClub.isPresent()) {
            logger.info("YEP: " + chosenClub.get().getClubName() + " Was found");
            model.addAttribute("activeClub", club.getId());
            model.addAttribute("activeClubId", chosenClub.get().getId());
            model.addAttribute("activeClubsPitches", chosenClub.get().getPitches());
            model.addAttribute("activeClubName", chosenClub.get().getClubName());
            List<Club> findClubs = clubRepository.findAll();
            model.addAttribute("clubs", findClubs);
            model.addAttribute("populatedClubs", findClubs);
            model.addAttribute("club", chosenClub);
            model.addAttribute("pitches", pitchRepository.findAll());
            return "clubset";
        }
        if (chosenPitch.isPresent()) {
            logger.info("YEP: " + chosenPitch.get().getName() + " Was found");
            logger.info("LETS SEE, WAS : " + activeClubName + " present?");
            if(!activeClubId.isEmpty()){
                Long activeClubIdNo = Long.parseLong(activeClubId);
                Optional<Club> activeClub = clubRepository.findById(activeClubIdNo);
                if (activeClub.isPresent()){
                    List<Pitch> activeClubsPitches = activeClub.get().getPitches();
                    activeClubsPitches.add(chosenPitch.get());
                    activeClub.get().setPitches(activeClubsPitches);
                    clubRepository.save(activeClub.get());
                    chosenPitch.get().setClub(activeClub.get());
                    pitchRepository.save(chosenPitch.get());
                }
            }
            List<Club> foundClubs = clubRepository.findAll();
            model.addAttribute("populatedClubs", foundClubs);
            model.addAttribute("pitches", pitchRepository.findAll());
            return "clubset";
        }

        model.addAttribute("pitches", pitchRepository.findAll());
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
