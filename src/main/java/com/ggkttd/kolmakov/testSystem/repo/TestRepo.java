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
}
