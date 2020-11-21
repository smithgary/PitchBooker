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

    /**
     * Not yet working properly
     * Should
     *      -Store the Clubrole into the db,
     *      -Update the User with the new Clubrole added,
     *      -Update the Club with the new Clubrole added,
     *      -Return the model with the updated UserClubRoles to be displayed in a table.
     */
    public ModelAndView saveClubRoleAndUser(ModelAndView modelAndView, ClubRole clubRole, String userId,
                                            UserRepository userRepository, ClubRepository clubRepository){
        //TODO: Check that logged in user has permission to access this.
        LOGGER.info("Request from user:{} to save details of user with id:{}", LoggedInUser.getLoggedInUser(), userId);

        Long usersId = Long.parseLong(userId);
        Optional<User> user = userRepository.findById(usersId);
        if(user.isPresent()) {
            Optional<Club> usersClub = clubRepository.findById(clubRole.getClub().getId());
            if (usersClub.isPresent()) {
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
// WILL BE->    modelAndView.addObject("usersClubRoles", user.get().getClubRoles());
                // OLD VERSION model.addAttribute("usersClubRoles", user.get().getClubRoles());
                // NOT NEEDED (Don't think) as will already be present // model.addAttribute("user", user.get());
            }
        }
        return modelAndView;
    }

    /**
     * Updates the Model. For the chosen user, gets user from the repository.
     * Calls to update the list of stored clubroles for that user.
     * Also calls to get the list of options for adding another clubrole
     */
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

    /**
     * Updates the model, adding the user, user id and list of stored ClubRoles for that user
     */
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
        modelAndView.addObject("groups", Group.values());
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
}

