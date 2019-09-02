package com.ggkttd.kolmakov.testSystem.repo;

import com.ggkttd.kolmakov.testSystem.domain.PassingTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PassingTestRepo extends JpaRepository<PassingTest,Long> {
}
