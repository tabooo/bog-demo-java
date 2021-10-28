package com.bog.demo.repository.user;

import com.bog.demo.domain.user.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

    Optional<User> findByEmailAndState(String email, Integer state);

    @Query("select u from User u")
    List<User> findAll();
}