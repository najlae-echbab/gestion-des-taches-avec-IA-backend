package com.backend.backend.repository;

import com.backend.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
        User findByUsername(String username);
        Optional<User> findById(Long id);



      /*  @Query("SELECT u.username FROM User u")
        List<String> findAllUsernames();*/
}
