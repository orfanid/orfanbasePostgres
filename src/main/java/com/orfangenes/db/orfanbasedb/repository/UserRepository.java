package com.orfangenes.db.orfanbasedb.repository;


import com.orfangenes.db.orfanbasedb.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Suresh Hewapathirana
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
