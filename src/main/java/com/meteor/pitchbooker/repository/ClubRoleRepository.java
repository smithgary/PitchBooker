package com.meteor.pitchbooker.repository;

import com.meteor.pitchbooker.domain.ClubRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface ClubRoleRepository extends JpaRepository<ClubRole, Long> {

}
