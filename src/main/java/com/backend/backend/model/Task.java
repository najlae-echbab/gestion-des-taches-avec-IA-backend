package com.backend.backend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titre;
    private String description;
    private String statut = "to do";
    private LocalDate dateEcheance;
    private int priorite;

    @ManyToOne
    @JoinColumn(name = "projet_id")
    @JsonBackReference("project-tasks")
    private Project projet;


    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference("user-tasks")
    private User utilisateur;
}
