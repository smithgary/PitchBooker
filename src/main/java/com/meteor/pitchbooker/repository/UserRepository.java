package com.meteor.pitchbooker.repository;

import com.meteor.pitchbooker.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query
    public List<User> findByEmail(String name);

}
