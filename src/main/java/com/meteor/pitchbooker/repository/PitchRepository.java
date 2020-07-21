package com.meteor.pitchbooker.repository;

import com.meteor.pitchbooker.Pitch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PitchRepository extends JpaRepository<Pitch, Long> {

    @Query
    public List<Pitch> findByName(String name);

}
