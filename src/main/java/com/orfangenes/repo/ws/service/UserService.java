package com.orfangenes.repo.ws.service;

import com.orfangenes.repo.ws.entity.User;
import com.orfangenes.repo.ws.exception.ResourceNotFoundException;
import com.orfangenes.repo.ws.exception.UserNotFoundException;
import com.orfangenes.repo.ws.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Suresh Hewapathirana
 */
@Transactional
@Service
public class UserService {

    private final UserRepository repository;

    @Autowired
    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public List<User> findAllUsers() {
        return repository.findAll();
    }

    public User getUser(long userId){
        return repository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }

    public User saveUser(User user){
        return repository.save(user);
    }

    public User updateUser(long userId, User userRequest){
        return  repository.findById(userId)
                .map(user -> {
                    user.setId(userRequest.getId());
                    user.setFirstName(userRequest.getFirstName());
                    user.setLastName(userRequest.getLastName());
                    user.setEmail(userRequest.getEmail());
                    return repository.save(user);
                }).orElseThrow(() -> new ResourceNotFoundException("User not found with id " + userId));
    }

    public ResponseEntity<?> deleteUser(long userId){
        return repository.findById(userId)
                .map(user -> {
                    repository.delete(user);
                    return ResponseEntity.ok().build();
                }).orElseThrow(() -> new ResourceNotFoundException("User not found with id " + userId));
    }

    public User getUserByEmail(String email) {
        return repository.findUserByEmail(email);
    }
}
