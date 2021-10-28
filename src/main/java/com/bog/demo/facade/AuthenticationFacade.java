package com.bog.demo.facade;

import com.bog.demo.model.user.UserDto;
import com.bog.demo.util.Descriptor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

public interface AuthenticationFacade {

    Authentication getAuthentication();

    UserDto getUser();
}