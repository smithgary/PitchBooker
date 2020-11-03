package com.meteor.pitchbooker.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;
import java.util.Map;

@Entity
public class Club {
 @Id
 @GeneratedValue
 private Long id;
 private String clubName;
 @OneToMany
 private List<Pitch> pitches;

 //private Map<User, List<Role, Group>> usersRoles;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClubName() {
        return clubName;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }

    public List<Pitch> getPitches() {
        return pitches;
    }

    public void setPitches(List<Pitch> pitches) {
        this.pitches = pitches;
    }
}
