package com.ggkttd.kolmakov.testSystem.repo;

import com.ggkttd.kolmakov.testSystem.domain.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepo extends JpaRepository<Group,Long> {
    @Query(value = "select * from group g where g.name like :name",nativeQuery = true)
    Group getGroupByName(@Param("name") String name);
}
