package com.quizserver.repository;

import com.quizserver.entities.Contest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContestRepository extends JpaRepository<Contest, Long> {

}
