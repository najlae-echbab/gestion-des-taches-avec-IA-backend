package com.backend.backend.controller;

import com.backend.backend.model.Task;
import com.backend.backend.repository.ProjectRepository;
import com.backend.backend.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    // ✅ Mettre à jour le statut uniquement
    @PutMapping("/taches/status/{id}")
    public ResponseEntity<?> updateTaskStatus(@PathVariable Long id, @RequestBody Map<String, String> payload) {
        Optional<Task> taskOptional = taskRepository.findById(id);
        if (taskOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Task task = taskOptional.get();
        String newStatus = payload.get("statut");

        if (newStatus != null && !newStatus.isBlank()) {
            task.setStatut(newStatus);
            taskRepository.save(task);
            return ResponseEntity.ok(task);
        } else {
            return ResponseEntity.badRequest().body("Le champ 'statut' est manquant ou vide.");
        }
    }

    // ✅ Mise à jour du titre et/ou statut
    @PutMapping("/taches/{id}")
    public ResponseEntity<?> updateTask(@PathVariable Long id, @RequestBody Map<String, String> payload) {
        Optional<Task> taskOptional = taskRepository.findById(id);
        if (taskOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Task task = taskOptional.get();
        String newTitre = payload.get("titre");
        String newStatut = payload.get("statut");

        boolean modified = false;

        if (newTitre != null && !newTitre.isBlank()) {
            task.setTitre(newTitre);
            modified = true;
        }

        if (newStatut != null && !newStatut.isBlank()) {
            task.setStatut(newStatut);
            modified = true;
        }

        if (!modified) {
            return ResponseEntity.badRequest().body("Aucun champ valide fourni.");
        }

        taskRepository.save(task);
        return ResponseEntity.ok(task);
    }

    @PostMapping("/taches")
    public ResponseEntity<Task> createTask(@RequestBody Task task, @RequestParam Long projectId) {
        return projectRepository.findById(projectId)
                .map(project -> {
                    task.setProjet(project);
                    Task savedTask = taskRepository.save(task);
                    return ResponseEntity.ok(savedTask);
                })
                .orElse(ResponseEntity.badRequest().build());
    }

    @DeleteMapping("/taches/{id}")
    public ResponseEntity<Void> deleteTache(@PathVariable Long id) {
        if (!taskRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        taskRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

