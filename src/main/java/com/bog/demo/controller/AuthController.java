package com.bog.demo.controller;

import com.bog.demo.domain.user.UserVerification;
import com.bog.demo.facade.AuthenticationFacade;
import com.bog.demo.model.user.UserDto;
import com.bog.demo.service.user.UserService;
import com.bog.demo.util.Descriptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Date;
import java.util.Objects;

import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

@RestController("authController")
@RequestMapping("rest/api/auth")
public class AuthController {

    @Resource(name = "authenticationManager")
    private AuthenticationManager authManager;

    private AuthenticationFacade authenticationFacade;

    private UserService userService;

    @GetMapping("login")
    public ResponseEntity<UserDto> login(@RequestParam("username") final String username,
                                         @RequestParam("password") final String password,
                                         final HttpServletRequest request, HttpServletResponse response) {
        UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken(username, password);
        Authentication auth = authManager.authenticate(authReq);

        SecurityContext sc = SecurityContextHolder.getContext();
        sc.setAuthentication(auth);

        UserDto user = authenticationFacade.getUser();

        if (user.getEnabled() == null || user.getEnabled() != 1) {
            logout(request, response);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "USER_NOT_VERIFIED");
        }

        HttpSession session = request.getSession(true);
        session.setAttribute("user", user);
        session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, sc);

        return new ResponseEntity<>(authenticationFacade.getUser(), HttpStatus.OK);
    }

    @GetMapping("logout")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (Objects.nonNull(authentication)) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("register")
    public ResponseEntity<Descriptor> register(@Valid @RequestBody UserDto userDto) {

        return new ResponseEntity<>(userService.register(userDto), HttpStatus.OK);
    }

    @GetMapping("check-verification")
    public ResponseEntity<Descriptor> checkVerification(@RequestParam String key) {
        UserVerification userVerification = userService.checkVerification(key);

        if (userVerification == null) {
            return new ResponseEntity<>(Descriptor.invalidDescriptor("USER_NOT_FOUND"), HttpStatus.OK);
        }
        if (userVerification.getExpireDate().getTime() < new Date().getTime()) {
            return new ResponseEntity<>(Descriptor.invalidDescriptor("EXPIRED"), HttpStatus.OK);
        }
        if (userVerification.getState() != 1) {
            return new ResponseEntity<>(Descriptor.invalidDescriptor("ALREADY_USED"), HttpStatus.OK);
        }

        return new ResponseEntity<>(Descriptor.validDescriptor(), HttpStatus.OK);
    }

    @PostMapping("set-password")
    public ResponseEntity<Descriptor> setPassword(@RequestParam String password, @RequestParam String key) {

        return new ResponseEntity<>(userService.setPassword(password, key), HttpStatus.OK);
    }

    @GetMapping("session-data")
    public ResponseEntity<UserDto> getSessionData() {

        return new ResponseEntity<>(authenticationFacade.getUser(), HttpStatus.OK);
    }

    @Autowired
    public void setAuthenticationFacade(AuthenticationFacade authenticationFacade) {
        this.authenticationFacade = authenticationFacade;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
