package com.orfangenes.db.orfanbasedb.service;

import com.orfangenes.db.orfanbasedb.entity.User;
import com.orfangenes.db.orfanbasedb.exception.ResourceNotFoundException;
import com.orfangenes.db.orfanbasedb.exception.UserNotFoundException;
import com.orfangenes.db.orfanbasedb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Suresh Hewapathirana
 */
@Service
public class UserService {

    @Autowired
    private UserRepository repository;

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
}
