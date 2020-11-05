package com.meteor.pitchbooker.web;

import com.meteor.pitchbooker.domain.Club;
import com.meteor.pitchbooker.domain.User;
import com.meteor.pitchbooker.repository.ClubRepository;
import com.meteor.pitchbooker.repository.UserRepository;
import com.meteor.pitchbooker.utils.LoggedInUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Optional;

@Controller
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ClubRepository clubRepository;

    private Logger logger = LoggerFactory.getLogger(UserController.class);

    //Only viewable by super users across all clubs
    @GetMapping("/users")
    public String showUsers(Model model) {
        List<User> users = userRepository.findAll();
        model.addAttribute("users", users);

        //allow new player to be created..
        model.addAttribute("user", new User());
        return "users";
    }

    @PostMapping("/users")
    public String addUser(@ModelAttribute User user, ModelMap model) {
        user.setEnabled(false);
        userRepository.save(user);

        List<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        logger.info("New user created - {}", user.getFirstName() + " " + user.getLastName());
        //allow new player to be created..
        model.addAttribute("user", new User());
        return "users";
    }

    @GetMapping("/user/{id}/roles")
    public String showUserRoles(Model model, @PathVariable String id) {
        String loggedInUser = LoggedInUser.getLoggedInUser();
        logger.info("Request from user:{} to show details of user with id: {}", loggedInUser, id);
        //TODO: Check that logged in user has permission to access this.
        //ie role is > level 80 or something.
        Long usersId = Long.parseLong(id);
        Optional<User> user = userRepository.findById(usersId);
        if(user.isPresent()){
            model.addAttribute("user", user.get());
            model.addAttribute("usersClubRoles", user.get().getClubRoles());
        }

        return "user-roles";
    }

    @GetMapping("/users/club")
    public String showClubUsers(Model model) {
        String user = LoggedInUser.getLoggedInUser();
        logger.info("Request from user:{} to show clubs", user);

        List<User> users = userRepository.findAll();
        model.addAttribute("users", users);

        List<Club> findClubs = clubRepository.findAll();
        model.addAttribute("clubs", findClubs);

        //allow new player to be created..
        model.addAttribute("user", new User());
        return "clubusers";
    }

    @PostMapping("/users/club")
    public String addClubUser(@ModelAttribute User user, @ModelAttribute Club club, ModelMap model) {
        user.setEnabled(false);
        userRepository.save(user);

        List<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        logger.info("New user created - {}", user.getFirstName() + " " + user.getLastName());
        //allow new player to be created..
        model.addAttribute("user", new User());
        return "clubusers";
    }

    @Autowired
    public void setClubRepository(ClubRepository clubRepository){
        this.clubRepository = clubRepository;
    }
}
