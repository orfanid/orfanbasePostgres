package com.orfangenes.repo.ws.repository;

import com.orfangenes.repo.ws.entity.Gene;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Suresh Hewapathirana
 */
@Repository
public interface GeneRepository extends JpaRepository<Gene, Long> {
    List<Gene> findByOrfanLevel(String orfanLevel);
}
