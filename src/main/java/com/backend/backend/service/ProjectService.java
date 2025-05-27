package com.backend.backend.service;

import com.backend.backend.model.Project;
import com.backend.backend.model.Task;
import com.backend.backend.model.User;
import com.backend.backend.repository.ProjectRepository;
import com.backend.backend.repository.TaskRepository;
import com.backend.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    @Autowired
    private OpenAIService openAIService;




    @Autowired
    public ProjectService(ProjectRepository projectRepository, TaskRepository taskRepository, UserRepository userRepository, UserService userService) {
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    public User getCurrentUser() {
        // Ta méthode getCurrentUser dans UserServiceImpl
        return userService.getCurrentUser();
    }
    public Project creerProjet(Project projet, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        projet.setChefDeProjet(user); // Associer le créateur du projet

        Project nouveauProjet = projectRepository.save(projet);

        //  Simulation de la génération de tâches par IA
        List<Task> tachesGenerees = genererTachesAvecIA(projet.getDescription());
        for (Task task : tachesGenerees) {
            task.setProjet(nouveauProjet);
            taskRepository.save(task);
        }

        return nouveauProjet;
    }

    //  l'appel à l'IA
    private List<Task> genererTachesAvecIA(String description) {
        List<Task> taches = new ArrayList<>();
        try {
            String reponse = openAIService.generateTasks(description);

            String[] lignes = reponse.split("\n");
            int ordre = 1;

            for (String ligne : lignes) {
                ligne = ligne.replaceAll("^[0-9]+\\.\\s*", "").trim();
                if (!ligne.isEmpty()) {
                    Task t = new Task(null, ligne, "", "En attente", LocalDate.now().plusDays(ordre * 2), ordre, null, null);
                    taches.add(t);
                    ordre++;
                }
            }

        } catch (Exception e) {
            System.out.println("Erreur OpenAI : " + e.getMessage());
        }
        return taches;
    }

    public List<Project> getProjectsByCurrentUser() {
        User currentUser = getCurrentUser();

        if (currentUser != null) {
            return projectRepository.findByChefDeProjet(currentUser); // Utilisation de findByChefDeProjet
        }
        throw new RuntimeException("Utilisateur non trouvé ou non authentifié");
    }
}