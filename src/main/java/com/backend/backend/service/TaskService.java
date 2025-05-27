package com.backend.backend.service;

import com.backend.backend.model.Task;
import com.backend.backend.model.User;
import com.backend.backend.repository.TaskRepository;
import com.backend.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskService(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    // Récupérer les tâches d'un projet
    public List<Task> getTachesParProject(Long projetId) {
        return taskRepository.findByProjetId(projetId);
    }

    //  Assigner une tâche à un utilisateur
    public Task assignerTache(Long taskId, Long userId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Tâche non trouvée"));

        User utilisateur = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        task.setUtilisateur(utilisateur);
        return taskRepository.save(task);
    }

}
