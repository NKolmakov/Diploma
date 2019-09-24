package com.ggkttd.kolmakov.testSystem.repo;

import com.ggkttd.kolmakov.testSystem.domain.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestRepo extends JpaRepository<Test, Long> {

    @Query(value = "select * from test t where t.subject_id in (select id from subject s where s.name like :name)", nativeQuery = true)
    List<Test> getTestsBySubjectName(@Param("name") String name);

    @Query(value = "select * from test t where t.subject_id = :id", nativeQuery = true)
    List<Test> getTestsBySubjectId(@Param("id") Long id);

    //returns list of tests, not passed by this user
    @Query(value = "select * from test t where " +
            "t.id not in(select test_id from passing_test p where user_id = :userId) and " +
            "t.subject_id = :subjectId", nativeQuery = true)
    List<Test> getNotPassedTestsBySubjectId(@Param("userId") Long userId, @Param("subjectId") Long subjectId);

    //list of passed tests
    @Query(value = "select * from test t where t.id in (select test_id from passing_test p where p.user_id = :id)",nativeQuery = true)
    List<Test> getPassedTestsByUser(@Param("id") Long id);

    @Query(value = "select * from test t where t.user_id = :id",nativeQuery = true)
    List<Test> getTestsByUserId(@Param("id") Long id);

    //returns list of tests, not passed by this user
    @Query(value = "select * from test t where t.id not in (select test_id from passing_test p where p.user_id = :id)",nativeQuery = true)
    List<Test> getNotPassedTestsByUser(@Param("id") Long id);
}
