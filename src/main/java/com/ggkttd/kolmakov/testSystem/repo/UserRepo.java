package com.ggkttd.kolmakov.testSystem.repo;

import com.ggkttd.kolmakov.testSystem.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User,Long> {
    @Query(value = "select * from user u where binary u.login like :login",nativeQuery = true)
    User getUserByLogin(@Param("login") String login);
}
