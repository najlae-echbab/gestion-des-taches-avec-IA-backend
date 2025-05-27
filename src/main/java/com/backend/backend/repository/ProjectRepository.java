package com.backend.backend.repository;

import com.backend.backend.model.Project;
import com.backend.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    //
    //
   // List<Project> findByUser(User user);
   List<Project> findByChefDeProjet(User chefDeProjet);
}
