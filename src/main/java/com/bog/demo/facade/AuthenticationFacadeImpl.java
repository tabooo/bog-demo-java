package com.bog.demo.facade;

import com.bog.demo.mapper.user.UserDataObjectMapper;
import com.bog.demo.model.user.UserDetailsImpl;
import com.bog.demo.model.user.UserDto;
import com.bog.demo.service.user.UserService;
import com.bog.demo.util.Descriptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class AuthenticationFacadeImpl implements AuthenticationFacade {

    private UserDataObjectMapper userDataObjectMapper;
    private UserService userService;

    @Override
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @Override
    public UserDto getUser() {
        Authentication authentication = getAuthentication();

        if (Objects.nonNull(authentication)) {
            Object principal = getAuthentication().getPrincipal();

            if (principal instanceof UserDetailsImpl) {
                return userDataObjectMapper.mapToDto((UserDetailsImpl) principal);
            }
        }

        return null;
    }

    @Autowired
    public void setUserDataObjectMapper(UserDataObjectMapper userDataObjectMapper) {
        this.userDataObjectMapper = userDataObjectMapper;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}