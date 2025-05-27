package com.backend.backend.controller;

import com.backend.backend.model.User;
import com.backend.backend.repository.UserRepository;
import com.backend.backend.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173") // Autoriser les requêtes depuis ton frontend React
@RestController
@Slf4j
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<User>> getAllUser() {
        try {
            System.out.println("📥 Requête reçue sur /api/users");
            List<User> users = userRepository.findAll();
            System.out.println("✅ Utilisateurs récupérés : " + users.size());
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            System.out.println("❌ Erreur lors de la récupération des utilisateurs : " + e.getMessage());
            return ResponseEntity.status(500).body(null);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<User> addUser(@RequestBody User user) {
        log.info("🚀 Enregistrement d'un nouvel utilisateur : " + user.getUsername());
        User savedUser = userService.saveUser(user);
        return ResponseEntity.ok(savedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            System.out.println("✅ Utilisateur supprimé avec succès (ID: " + id + ")");
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            System.out.println("❌ Erreur lors de la suppression de l'utilisateur : " + e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }
}
