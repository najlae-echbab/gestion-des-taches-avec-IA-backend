package com.backend.backend.controller;

import com.backend.backend.model.Project;
import com.backend.backend.model.Task;
import com.backend.backend.model.User;
import com.backend.backend.repository.ProjectRepository;
import com.backend.backend.service.ProjectService;
import com.backend.backend.service.TaskService;
import com.backend.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projets")
@CrossOrigin(origins = "http://localhost:5173")
public class ProjectController {

    private final ProjectService projectService;
    private final ProjectRepository projectRepository;
    private final TaskService taskService;

    @Autowired
    public ProjectController(ProjectService projectService, ProjectRepository projectRepository, TaskService taskService) {
        this.projectService = projectService;
        this.projectRepository = projectRepository;
        this.taskService = taskService;
    }

    // ✅ Création d'un projet
    @PostMapping("/create")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Project> creerProjet(@RequestBody Project projetCree, @RequestParam Long userId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        System.out.println("Utilisateur authentifié : " + username);
        System.out.println("UserId fourni : " + userId);

        Project projetSauve = projectService.creerProjet(projetCree, userId);

        // ✅ Retourne l'objet sauvé, qui contient tasks + user
        return ResponseEntity.ok(projetSauve);
    }

    // ✅ Récupérer toutes les tâches d'un projet
    @GetMapping("/{projetId}/taches")
    public ResponseEntity<List<Task>> getTachesParProjet(@PathVariable Long projetId) {
        List<Task> taches = taskService.getTachesParProject(projetId);
        return ResponseEntity.ok(taches);
    }

    // ✅ Récupérer tous les projets du user connecté
    @GetMapping
    public ResponseEntity<List<Project>> getAllProjects() {
        User currentUser = projectService.getCurrentUser();

        if (currentUser != null) {
            List<Project> projects = projectRepository.findByChefDeProjet(currentUser);
            return ResponseEntity.ok(projects);
        }

        return ResponseEntity.status(403).build();
    }
}
