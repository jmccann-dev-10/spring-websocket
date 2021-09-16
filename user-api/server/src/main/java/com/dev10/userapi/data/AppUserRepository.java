package com.dev10.userapi.data;

import com.dev10.userapi.models.AppUser;
import org.springframework.transaction.annotation.Transactional;

public interface AppUserRepository {
    AppUser findByUsername(String username);
    AppUser create(AppUser user);
}
