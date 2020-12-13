package com.meteor.pitchbooker.web;

import com.meteor.pitchbooker.domain.*;
import com.meteor.pitchbooker.repository.ClubRepository;
import com.meteor.pitchbooker.repository.ClubRoleRepository;
import com.meteor.pitchbooker.repository.UserRepository;
import com.meteor.pitchbooker.service.UserControllerHelper;
import com.meteor.pitchbooker.utils.LoggedInUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.security.RolesAllowed;

@Controller
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ClubRepository clubRepository;
    @Autowired
    public void setClubRepository(ClubRepository clubRepository){
        this.clubRepository = clubRepository;
    }
    @Autowired
    public void setClubRoleRepository(ClubRoleRepository clubRoleRepository){
        this.clubRoleRepository = clubRoleRepository;
    }
    private Logger logger = LoggerFactory.getLogger(UserController.class);
    private ClubRoleRepository clubRoleRepository;

    private UserControllerHelper userControllerHelper = new UserControllerHelper();


    @GetMapping("/users")
    @RolesAllowed("USER")
    public ModelAndView showUsers() {
        return userControllerHelper
                .addAllUsersWithNewUser(new ModelAndView("users"), userRepository);
    }

    @RolesAllowed("ADMIN")
    @PostMapping("/users")
    public ModelAndView addUser(@ModelAttribute User user) {
        userControllerHelper.saveUser(user, userRepository);

        return userControllerHelper
                .addAllUsersWithNewUser(new ModelAndView("users"), userRepository);
    }


    @GetMapping("/user/{id}/roles")
    public ModelAndView showUserRoles(Model model, @PathVariable String id) {
        return userControllerHelper
                .addRoleInfoForUser(new ModelAndView("user-roles"), id, userRepository, clubRepository);
    }

    @PostMapping("/user/{id}/roles")
    public ModelAndView addUserRoles(@ModelAttribute ClubRole clubRole,
                                     @PathVariable String id) {
        ModelAndView roleInfoWithUser = userControllerHelper
                .addRoleInfoForUser(new ModelAndView("user-roles"), id, userRepository, clubRepository);

        return userControllerHelper
                .saveClubRoleAndUser(roleInfoWithUser, clubRole, id, userRepository, clubRepository, clubRoleRepository);
    }


    @GetMapping("/users/club")
    public ModelAndView showClubUsers(Model model) {
        String user = LoggedInUser.getLoggedInUser();
        logger.info("Request from user:{} to show clubs", user);
        return userControllerHelper
                .addAllClubsAndUsersWithNewUser(new ModelAndView("clubusers"), userRepository, clubRepository);

    }

    @PostMapping("/users/club")
    public ModelAndView addClubUser(@ModelAttribute User user, @ModelAttribute Club club) {

        userControllerHelper.saveUser(user, userRepository);

        return userControllerHelper
                .addAllUsersWithNewUser(new ModelAndView("clubusers"), userRepository);
    }


}
