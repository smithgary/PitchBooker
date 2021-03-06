package com.meteor.pitchbooker.service;

import com.meteor.pitchbooker.domain.*;
import com.meteor.pitchbooker.repository.ClubRepository;
import com.meteor.pitchbooker.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class UserControllerHelperTest {

    @Autowired
    private UserRepository userRepository = mock(UserRepository.class);
    @Autowired
    private ClubRepository clubRepository = mock(ClubRepository.class);
    @Mock
    private Model model;
    @Mock
    private MockMvc mockMvc;
    private List<User> usersInDb;
    private List<Club> clubsInDb;
    private User newUser;

    @BeforeEach
    public void setup(){
        User first = new User();
        first.setUserId(1L);
        first.setFirstName("George");
        first.setLastName("Wiley");

        User second = new User();
        second.setUserId(2L);
        second.setFirstName("Karen");
        second.setLastName("Molloy");

        newUser = new User();  //Not added to usersInDb
        newUser.setUserId(3L);
        newUser.setFirstName("Tom");
        newUser.setLastName("Markham");

        usersInDb = new ArrayList<>();
        usersInDb.add(first);
        usersInDb.add(second);

        Club club1 = new Club();
        club1.setClubName("A great Club");
        Club club2 = new Club();
        club2.setClubName("A rival Club");
        Club club3 = new Club();
        club3.setClubName("Challenger");
        clubsInDb = new ArrayList<>();
        clubsInDb.add(club1);
        clubsInDb.add(club2);
        clubsInDb.add(club3);

    }

    @Test
    public void testAddAllUsersWithNewUser() throws Exception {

        when(userRepository.findAll()).thenReturn(usersInDb);

        ModelAndView trialTestModelAndView = new ModelAndView("users");
        UserControllerHelper userControllerHelper = new UserControllerHelper();

        ModelAndView testModelAndView = userControllerHelper.addAllUsersWithNewUser(trialTestModelAndView, userRepository);

        /**
         * Tests:
         * 1. Model should contain 2 users
         * 2. Model item 1 should have first name George and last name Wiley
         * 3. View should be "users"
         */

        ModelAndViewAssert.assertAndReturnModelAttributeOfType(testModelAndView, "users", ArrayList.class);
        ModelAndViewAssert.assertCompareListModelAttribute(testModelAndView, "users", usersInDb);
        ModelAndViewAssert.assertModelAttributeAvailable(testModelAndView, "user");
        ModelAndViewAssert.assertViewName(testModelAndView, "users");
    }

    @Test
    public void testAddAllClubsAndUsersWithNewUser() throws Exception {

        when(userRepository.findAll()).thenReturn(usersInDb);
        when(clubRepository.findAll()).thenReturn(clubsInDb);

        ModelAndView trialTestModelAndView = new ModelAndView("clubusers");
        UserControllerHelper userControllerHelper = new UserControllerHelper();

        ModelAndView testModelAndView = userControllerHelper.addAllClubsAndUsersWithNewUser(trialTestModelAndView, userRepository, clubRepository);

        /**
         * Tests:
         * 1. Model should contain 2 users
         * 2. Model item 1 should have first name George and last name Wiley
         * 3. View should be "clubusers"
         * 4. Clubs should contain an ArrayList
         * 5. Clubs should match clubsInDb
         */

        ModelAndViewAssert.assertAndReturnModelAttributeOfType(testModelAndView, "users", ArrayList.class);
        ModelAndViewAssert.assertCompareListModelAttribute(testModelAndView, "users", usersInDb);
        ModelAndViewAssert.assertModelAttributeAvailable(testModelAndView, "user");

        ModelAndViewAssert.assertAndReturnModelAttributeOfType(testModelAndView, "clubs", ArrayList.class);
        ModelAndViewAssert.assertCompareListModelAttribute(testModelAndView, "clubs", clubsInDb);

        ModelAndViewAssert.assertViewName(testModelAndView, "clubusers");
    }
    @Test
    public void testSaveClubRoleAndUser(){
        //Todo: Create builders for ClubRole etc.
        /**
         * Tests:
         * 1. User in the db, without a ClubRole already stored
         */
        when(userRepository.findAll()).thenReturn(usersInDb);
        when(userRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(usersInDb.get(1)));
        when(clubRepository.findAll()).thenReturn(clubsInDb);
        when(clubRepository.findById(1L)).thenReturn(Optional.ofNullable(clubsInDb.get(1)));

        Club club = new Club(); club.setId(1L); club.setClubName("TrialClub");
        ClubRole clubRole = new ClubRole();
        clubRole.setClub(club);
        clubRole.setUser(usersInDb.get(1));
        clubRole.setAgeGrouping(AgeGrouping.UNDER_12);
        clubRole.setCode(Code.HURLING);
        clubRole.setRole(Role.PLAYER);
        clubRole.setYear(Year.now());

        ModelAndView trialTestModelAndView = new ModelAndView("user-roles");
        UserControllerHelper userControllerHelper = new UserControllerHelper();

        //ModelAndView testModelAndView = userControllerHelper.saveClubRoleAndUser(trialTestModelAndView, clubRole, "1", userRepository, clubRepository);
    }

    @Test
    public void testSaveUser(){
        when(userRepository.findAll()).thenReturn(usersInDb);
        UserControllerHelper userControllerHelper = new UserControllerHelper();
        userControllerHelper.saveUser(newUser, userRepository);
        //How do I test this..?
        System.out.println("Size : " + userRepository.findAll().size());

    }


}
