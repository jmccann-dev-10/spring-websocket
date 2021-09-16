package com.dev10.userapi.controllers;

import com.dev10.userapi.domain.AppUserService;
import com.dev10.userapi.domain.Result;
import com.dev10.userapi.models.AppUser;
import com.dev10.userapi.utility.JwtConverter;
import com.dev10.userapi.validation.ValidationErrorResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class AppUserController {
    private final AppUserService appUserService;
    private final JwtConverter jwtConverter;
    private final PasswordEncoder passwordEncoder;

    public AppUserController(AppUserService appUserService, JwtConverter jwtConverter,
                             PasswordEncoder passwordEncoder) {
        this.appUserService = appUserService;
        this.jwtConverter = jwtConverter;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/create_account")
    public ResponseEntity<?> createAccount(@RequestBody AppUser user) {
        // Hard reset the roles so that the client can't attempt to set their own role(s).
        user.setRoles(List.of("USER"));

        Result<AppUser> result = appUserService.create(user);

        // Unhappy path...

        if (!result.isSuccess()) {
            ValidationErrorResult validationErrorResult = new ValidationErrorResult();
            validationErrorResult.addMessages(result.getMessages());
            return new ResponseEntity<>(validationErrorResult, HttpStatus.BAD_REQUEST);
        }

        // Happy path...

        HashMap<String, String> map = new HashMap<>();
        map.put("id", user.getId());

        return new ResponseEntity<>(map, HttpStatus.CREATED);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        // Make sure that we have a username and password...
        if (username != null && !username.isBlank() && password != null && !password.isBlank()) {
            AppUser user = appUserService.findByUsername(username);

            if (user != null && passwordEncoder.matches(password, user.getPassword())) {
                String jwtToken = jwtConverter.getTokenFromUser(user);

                HashMap<String, String> map = new HashMap<>();
                map.put("jwt_token", jwtToken);

                return new ResponseEntity<>(map, HttpStatus.OK);
            }
        }

        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @PostMapping("/refresh_token")
    public ResponseEntity<Map<String, String>> refreshToken(@RequestBody Map<String, String> body) {
        String token = body.get("jwt_token");

        AppUser user = jwtConverter.getUserFromToken(token);

        if (user != null) {
            String jwtToken = jwtConverter.getTokenFromUser(user);

            HashMap<String, String> map = new HashMap<>();
            map.put("jwt_token", jwtToken);

            return new ResponseEntity<>(map, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
}
