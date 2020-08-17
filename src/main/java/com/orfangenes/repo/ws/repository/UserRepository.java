package com.orfangenes.repo.ws.repository;


import com.orfangenes.repo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Suresh Hewapathirana
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
