package com.meteor.pitchbooker.repository;

import com.meteor.pitchbooker.domain.Club;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ClubRepository extends JpaRepository<Club, Long> {
    @Query
    public List<Club> findByClubName(String name);

}
