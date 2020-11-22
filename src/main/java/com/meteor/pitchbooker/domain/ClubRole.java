package com.meteor.pitchbooker.domain;

import javax.persistence.*;
import java.time.Year;

@Entity
public class ClubRole {
    @Id
    @GeneratedValue
    private Long id;
    private Code code;
    private AgeGrouping ageGrouping;
    private Role role;
    private Year year;

    @ManyToOne
    private Club club;
    @ManyToOne
    private User user;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    public Club getClub() {
        return club;
    }

    public void setClub(Club club) { this.club = club;}

    public Code getCode() {
        return code;
    }

    public void setCode(Code code) {
        this.code = code;
    }

    public AgeGrouping getAgeGrouping() {
        return ageGrouping;
    }

    public void setAgeGrouping(AgeGrouping ageGrouping) {
        this.ageGrouping = ageGrouping;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Year getYear() {
        return year;
    }

    public void setYear(Year year) {
        this.year = year;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
