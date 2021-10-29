package com.bog.demo.mapper.user;

import com.bog.demo.domain.user.User;
import com.bog.demo.model.user.UserDetailsImpl;
import com.bog.demo.model.user.UserDto;
import org.springframework.stereotype.Component;

@Component
public class UserDataObjectMapper {

    public UserDto mapToDto(User user) {
        UserDto dto = new UserDto();

        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setAccountNumber(user.getAccountNumber());
        dto.setEnabled(user.getEnabled());
        dto.setId(user.getId());
        dto.setState(user.getState());
        dto.setEnabled(user.getEnabled());

        return dto;
    }

    public User mapToUser(UserDto dto) {
        User user = new User();

        user.setId(dto.getId());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setAccountNumber(dto.getAccountNumber());
        user.setEnabled(dto.getEnabled());
        user.setState(dto.getState());
        user.setBalance(dto.getBalance());

        return user;
    }

    public UserDto mapToDto(UserDetailsImpl user) {
        UserDto dto = new UserDto();

        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setAccountNumber(user.getAccountNumber());
        dto.setEnabled(user.getEnabled());
        dto.setState(user.getState());

        return dto;
    }
}