package com.meteor.pitchbooker.service;

import com.meteor.pitchbooker.domain.*;
import com.meteor.pitchbooker.repository.ClubRepository;
import com.meteor.pitchbooker.repository.UserRepository;
import com.meteor.pitchbooker.utils.LoggedInUser;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserControllerHelper {
    private org.slf4j.Logger LOGGER = LoggerFactory.getLogger(UserControllerHelper.class);

    public ModelAndView addAllUsersWithNewUser(ModelAndView modelAndView, UserRepository userRepository){
        List<User> users = userRepository.findAll();
        modelAndView.addObject("users", users);

        modelAndView.addObject("user", new User());

        return modelAndView;
    }

    public ModelAndView addAllClubsAndUsersWithNewUser(ModelAndView modelAndView, UserRepository userRepository, ClubRepository clubRepository){

        ModelAndView withNewUser = addAllUsersWithNewUser(modelAndView, userRepository);

        List<Club> clubs = clubRepository.findAll();
        withNewUser.addObject("clubs", clubs);

        return withNewUser;
    }

    public ModelAndView addRoleInfoForUser(ModelAndView modelAndView, String userId, UserRepository userRepository, ClubRepository clubRepository){
       //Todo: Check that the loggedin user has the correct privilege to do this.
        String loggedInUser = LoggedInUser.getLoggedInUser();
        LOGGER.info("Request from user:{} to show details of user with id: {}", loggedInUser, userId);
        Long usersId = Long.parseLong(userId);
        Optional<User> user = userRepository.findById(usersId);

        if(user.isPresent()){
            //Would like to do this without mutating the ModelAndView Object
            modelAndView = addUserClubRoles(modelAndView, user);
        }

        modelAndView = addClubRoleOptions(modelAndView, clubRepository);

        return modelAndView;
    }
    private ModelAndView addUserClubRoles(ModelAndView modelAndView, Optional<User> user){
        modelAndView.addObject("user", user.get());
        modelAndView.addObject("userId", user.get().getUserId().toString());
        modelAndView.addObject("usersClubRoles", user.get().getClubRoles());
        return modelAndView;
    }

    private ModelAndView addClubRoleOptions(ModelAndView modelAndView, ClubRepository clubRepository){
        modelAndView.addObject("clubRole", new ClubRole());
        //Add enums
        List<Club> listOfClubs = clubRepository.findAll();
        modelAndView.addObject("clubs", listOfClubs);
        modelAndView.addObject("groups", Group.values());
        modelAndView.addObject("codes", Code.values());
        modelAndView.addObject("roles", Role.values());
        List<Year> yearOptions = new ArrayList<>();
        yearOptions.add(Year.now().plusYears(1));
        yearOptions.add(Year.now());
        yearOptions.add(Year.now().minusYears(1));
        modelAndView.addObject("seasons", yearOptions);

        return modelAndView;
    }
    public void saveUser(User user, UserRepository userRepository){
        user.setEnabled(false);
        userRepository.save(user);
        LOGGER.info("New user stored - {}", user.getFirstName() + " " + user.getLastName());
    }
}

