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
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.security.RolesAllowed;

import java.time.Year;
import java.util.*;

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
    public String addUserRoles(Model model, @ModelAttribute ClubRole clubRole, @PathVariable String id) {
        String loggedInUser = LoggedInUser.getLoggedInUser();
        logger.info("Request from user:{} to save details of user with id: {}", loggedInUser, id);
        //TODO: Check that logged in user has permission to access this.
        //ie role is > level 80 or something.
        Long usersId = Long.parseLong(id);
        Optional<User> user = userRepository.findById(usersId);
        if(user.isPresent()){
            Optional<Club> usersClub = clubRepository.findById(clubRole.getClub().getId());
            if(usersClub.isPresent()) {
                clubRole.setClub(usersClub.get());
                System.out.println(clubRole.getClub().getClubName());//Save clubRole info..
                System.out.println(clubRole.getCode());
                System.out.println(clubRole.getGroup());
                System.out.println(clubRole.getRole());
                System.out.println(clubRole.getYear());
                clubRole.setUser(user.get());
                System.out.println(clubRole.getUser().getFirstName() + " " + clubRole.getUser().getLastName());
                // Save the Clubrole to the db
                //clubRoleRepository.save(clubRole);

                //Update the User and Save the user, with the new clubrole in the db
                Set<ClubRole> usersClubRoles = user.get().getClubRoles();
                usersClubRoles.add(clubRole);
                user.get().setClubRoles(usersClubRoles);
                userRepository.save(user.get());

                // Update the Club and Save the club, with the new clubrole in the db
//                Set<ClubRole> clubsClubRoles = usersClub.get().getClubRoles();
//                clubsClubRoles.add(clubRole);
//                usersClub.get().setClubRoles(clubsClubRoles);
//                clubRepository.save(usersClub.get());

                //model.addAttribute("usersClubRoles", user.get().getClubRoles());
                model.addAttribute("user", user.get());
            }
        }
        //Add enums
        List<Club> listOfClubs = clubRepository.findAll();
        model.addAttribute("clubs", listOfClubs);
        model.addAttribute("groups", Group.values());
        model.addAttribute("codes", Code.values());
        model.addAttribute("roles", Role.values());
        List<Year> yearOptions = new ArrayList<>();
        yearOptions.add(Year.now().plusYears(1));
        yearOptions.add(Year.now());
        yearOptions.add(Year.now().minusYears(1));
        model.addAttribute("seasons", yearOptions);
        return "user-roles";
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
