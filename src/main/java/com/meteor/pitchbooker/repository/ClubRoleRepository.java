package com.meteor.pitchbooker.repository;

import com.meteor.pitchbooker.domain.ClubRole;
import com.meteor.pitchbooker.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface ClubRoleRepository extends JpaRepository<ClubRole, Long> {

    @Query
    public Set<ClubRole> findAllByClub(String clubName);

}
