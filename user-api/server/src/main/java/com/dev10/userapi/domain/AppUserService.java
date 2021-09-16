package com.dev10.userapi.domain;

import com.dev10.userapi.data.AppUserRepository;
import com.dev10.userapi.models.AppUser;
import com.dev10.userapi.utility.UUIDGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

@Service
public class AppUserService {
    private enum ValidationMode {
        CREATE, UPDATE;
    }

    private final AppUserRepository repository;
    private final UUIDGenerator uuidGenerator;
    private final Validator validator;
    private final PasswordEncoder passwordEncoder;

    public AppUserService(AppUserRepository repository, UUIDGenerator uuidGenerator,
                          Validator validator, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.uuidGenerator = uuidGenerator;
        this.validator = validator;
        this.passwordEncoder = passwordEncoder;
    }

    public AppUser findByUsername(String username) {
        return repository.findByUsername(username);
    }

    public Result<AppUser> create(AppUser user) {
        Result<AppUser> result = validate(user, ValidationMode.CREATE);

        if (result.isSuccess()) {
            user.setId(uuidGenerator.getUUIDString());
            user.setPassword(passwordEncoder.encode(user.getPassword()));

            user = repository.create(user);
            result.setPayload(user);
        }

        return result;
    }

    private Result<AppUser> validate(AppUser user, ValidationMode validationMode) {
        Result<AppUser> result = new Result<>();

        if (user == null) {
            result.addMessage("User cannot be null.", ResultType.INVALID);
        } else if (validationMode == ValidationMode.CREATE && user.getId() != null && !user.getId().isBlank()) {
            result.addMessage("`id` should not be set.", ResultType.INVALID);
        } else if (validationMode == ValidationMode.UPDATE && (user.getId() == null || user.getId().isBlank())) {
            result.addMessage("`id` is required.", ResultType.INVALID);
        }

        if (result.isSuccess()) {
            Set<ConstraintViolation<AppUser>> violations = validator.validate(user);

            if (!violations.isEmpty()) {
                for (ConstraintViolation<AppUser> violation : violations) {
                    result.addMessage(violation.getMessage(), ResultType.INVALID);
                }
            }
        }

        if (result.isSuccess()) {
            AppUser existingUser = repository.findByUsername(user.getUsername());

            if (existingUser != null && !existingUser.getId().equalsIgnoreCase(user.getId())) {
                result.addMessage("`username` is in use by an existing user.", ResultType.INVALID);
            }
        }

        return result;
    }
}
