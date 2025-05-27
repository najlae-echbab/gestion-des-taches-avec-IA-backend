package com.backend.backend.service;
import com.backend.backend.model.User;
import java.util.Collection;
import java.util.List;

public interface UserService {
    User findByUsername(String username);
    User saveUser(User user);
    Collection<User> getAllUsers();
    void deleteUser(Long id);
    List<User> getAllMembers();
    User getCurrentUser();
    
}