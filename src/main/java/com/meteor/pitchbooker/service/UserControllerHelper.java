package com.meteor.pitchbooker.service;

import com.meteor.pitchbooker.domain.*;
import com.meteor.pitchbooker.repository.ClubRepository;
import com.meteor.pitchbooker.repository.ClubRoleRepository;
import com.meteor.pitchbooker.repository.UserRepository;
import com.meteor.pitchbooker.utils.LoggedInUser;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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

     public ModelAndView saveClubRoleAndUser(ModelAndView modelAndView,
                                            ClubRole clubRole,
                                            String userId,
                                            UserRepository userRepository,
                                            ClubRepository clubRepository,
                                            ClubRoleRepository clubRoleRepository){
        //TODO: Check that logged in user has permission to access this.

        Long usersId = Long.parseLong(userId);
        Optional<User> user = userRepository.findById(usersId);
        if(user.isPresent()) {
            Optional<Club> usersClub = clubRepository.findById(clubRole.getClub().getId());
            if (usersClub.isPresent()) {
                saveClubRole(clubRoleRepository, clubRole, usersClub, user);
                modelAndView = addUserClubRoles(modelAndView,  user);
            }
        }
        return modelAndView;
    }


    public void saveClubRole(ClubRoleRepository clubRoleRepository, ClubRole clubRole, Optional<Club> usersClub, Optional<User> user){

        clubRole.setClub(usersClub.get());
        clubRole.setUser(user.get());
        clubRoleRepository.save(clubRole);

    }

    public ModelAndView addRoleInfoForUser(ModelAndView modelAndView, String userId, UserRepository userRepository, ClubRepository clubRepository){
       //Todo: Check that the loggedin user has the correct privilege to do this.
        String loggedInUser = LoggedInUser.getLoggedInUser();
        LOGGER.info("Request from user:{} to show details of user with id: {}", loggedInUser, userId);
        Long usersId = Long.parseLong(userId);
        Optional<User> user = userRepository.findById(usersId);

        if(user.isPresent()){
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

    /**
     * Updates the model, adding attributes for each ClubRole dropdown item
     */
    private ModelAndView addClubRoleOptions(ModelAndView modelAndView, ClubRepository clubRepository){
        modelAndView.addObject("clubRole", new ClubRole());
        //Add enums
        List<Club> listOfClubs = clubRepository.findAll();
        modelAndView.addObject("clubs", listOfClubs);
        modelAndView.addObject("ageGroups", AgeGrouping.values());
        modelAndView.addObject("codes", Code.values());
        modelAndView.addObject("roles", Role.values());
        List<Year> yearOptions = new ArrayList<>();
        yearOptions.add(Year.now().minusYears(1));
        yearOptions.add(Year.now());
        yearOptions.add(Year.now().plusYears(1));
        modelAndView.addObject("seasons", yearOptions);

        return modelAndView;
    }

    /**
     * Saves the user into the user repository
     */
    public void saveUser(User user, UserRepository userRepository){
        user.setEnabled(false);
        userRepository.save(user);
        LOGGER.info("New user stored - {}", user.getFirstName() + " " + user.getLastName());
    }

    //Helper functions to print to console
    public void printClubRoleDetails(ClubRole clubRole, Club club, User user){
        System.out.println("ClubRole clubrole (club, clubname) passed in is:" + clubRole.getClub().getClubName());
        System.out.println("ClubRole clubrole (id) passed in is:" + clubRole.getId());
        System.out.println("Optional<Club> usersClub passed in is:" + club.getClubName());
        System.out.println("User user passed in is:" + user.getFirstName() + " " + user.getLastName());
    }

    public void printClubRoleInfo(ClubRole clubRole){
        System.out.println(clubRole.getClub().getClubName());//Save clubRole info..
        System.out.println(clubRole.getCode());
        System.out.println(clubRole.getAgeGrouping());
        System.out.println(clubRole.getRole());
        System.out.println(clubRole.getYear());
    }

    public void printClubRoles(Set<ClubRole> usersClubRoles){
        usersClubRoles.stream().forEach(cr -> {
            System.out.println("*ClubName* :" + cr.getClub().getClubName() +
                    " #ClubId# :" + cr.getClub().getId() +
                    " ### :" + cr.getAgeGrouping() +
                    " ### :" + cr.getCode() +
                    " ### :" + cr.getRole() +
                    " ### :" + cr.getYear() +
                    " ### :" + cr.getId());
        });
    }

    //NOT NEEDED??
    public User updateUserWithClubRole(Optional<User> user, ClubRole clubRole,
                                       ClubRoleRepository clubRoleRepository, UserRepository userRepository){
        /**
         * Update the User and Save the user, with the new clubrole in the db
         * ClubRole has been saved at this point, referred to as clubRole
         *
         * So..
         * 1. Get User's Set of ClubRoles
         * 2. Add the newly saved ClubRole to that set
         * 3. Save the User with the new set of Clubroles.
         */
        //1. Get the User's set of Clubroles
        System.out.println("Saving Club with new clubrole");
        Set<ClubRole> usersClubRoles = user.get().getClubRoles();
        System.out.println("UsersClubRoles size : " +  usersClubRoles.size());// ? "None" : usersClubRoles.forEach(roles -> roles.getId()));
        usersClubRoles.add(clubRole);
        //usersClubRoles.add(clubRoleRepository.getOne(clubRole.getId()));    //Issue here?
        System.out.println("After adding one, size is now: " + usersClubRoles.size());

        user.get().setClubRoles(usersClubRoles);
        //printClubRoles(usersClubRoles);
        //Getting error with next line: Unable to find ClubRole with id X, where X is the userId - should be the ClubRoleId.!
        //userRepository.save(user.get());  //Maybe don't need to do this?
        return user.get();
    }
}

