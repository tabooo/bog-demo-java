package com.bog.demo.repository.user;

import com.bog.demo.domain.user.UserVerification;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserVerificationRepository extends CrudRepository<UserVerification, Integer> {
    UserVerification findByKey(String key);
}