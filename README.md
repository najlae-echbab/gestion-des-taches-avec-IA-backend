#  Backend – Gestion des Tâches avec l'IA

## Présentation

Ce backend est le **moteur logique** de l'application **Gestion des Tâches avec l'IA**. Il fournit une API REST sécurisée avec **Spring Boot** pour gérer :

-  L’authentification des utilisateurs (avec JWT)
-  La création et gestion de projets
-  La création, modification et suppression des tâches
-  L’intégration avec l’API OpenAI pour générer automatiquement une liste de tâches à partir d'une simple description

---

##  Technologies utilisées

| Catégorie        | Outils / Librairies                     |
|------------------|------------------------------------------|
| Langage          | Java 17                                 |
| Framework        | Spring Boot                             |
| Authentification | Spring Security + JWT                   |
| ORM              | Spring Data JPA                         |
| Base de données  | MySQL                                   |
| IA               | OpenAI API                              |
         

---

##  Configuration du projet

###  Prérequis

- Java JDK 17 ou plus
- MySQL (avec un utilisateur et mot de passe configurés)
- Maven (`mvn`) ou wrapper (`./mvnw`)

###  Clonage du dépôt

```bash
git clone https://github.com/najlae-echbab/gestion-des-taches-avec-IA-backend.git
cd gestion-des-taches-avec-IA-backend
