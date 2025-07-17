package org.example.codebase.repository;

import org.example.codebase.entity.Shedlock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShedlockRepository extends JpaRepository<Shedlock, String> {
}