package com.meteor.pitchbooker.domain;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
public class Club {
 @Id
 @GeneratedValue
 private Long id;
 private String clubName;
 @OneToMany
 private List<Pitch> pitches;
 @OneToMany
 private Set<ClubRole> clubRoles;

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

    @OneToMany(mappedBy = "club")
    public Set<ClubRole> getClubRoles() {
        return clubRoles;
    }

    public void setClubRoles(Set<ClubRole> clubRoles) {
        this.clubRoles = clubRoles;
    }
}
