package com.ggkttd.kolmakov.testSystem.repo;

import com.ggkttd.kolmakov.testSystem.domain.PassingTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PassingTestRepo extends JpaRepository<PassingTest,Long> {

    @Query(value = "select * from passing_test p where p.user_id = :id",nativeQuery = true)
    List<PassingTest> getByUserId(@Param("id") Long id);

    @Query(value = "select * from passing_test p where p.test_id = :id",nativeQuery = true)
    List<PassingTest> getByTestId(@Param("id") Long testId);
}
