package com.bog.demo.service.user;

import com.bog.demo.domain.user.UserVerification;
import com.bog.demo.model.user.UserDto;
import com.bog.demo.util.Descriptor;

public interface UserService {
    Descriptor register(UserDto user);

    UserVerification checkVerification(String key);

    Descriptor setPassword(String password, String key);
}