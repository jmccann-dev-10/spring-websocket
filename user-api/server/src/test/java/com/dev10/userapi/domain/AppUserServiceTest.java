package com.dev10.userapi.domain;

import com.dev10.userapi.data.AppUserRepository;
import com.dev10.userapi.models.AppUser;
import com.dev10.userapi.utility.UUIDGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class AppUserServiceTest {
    @MockBean
    AppUserRepository repository;

    @MockBean
    UUIDGenerator uuidGenerator;

    @Autowired
    AppUserService service;

    @Autowired
    PasswordEncoder encoder;

    @Test
    void shouldFindByUsername() {
        AppUser expected = new AppUser(
                "983f1224-af4f-11eb-8368-0242ac110002",
                "johnsmith",
                "$2a$10$ntB7CsRKQzuLoKY3rfoAQen5nNyiC/U60wBsWnnYrtQQi8Z3IZzQa",
                false,
                "ADMIN");

        expected.setFirstName("John");
        expected.setLastName("Smith");
        expected.setEmailAddress("john@smith.com");
        expected.setMobilePhone("555-555-5555");

        when(repository.findByUsername(any())).thenReturn(expected);

        AppUser actual = service.findByUsername("johnsmith");

        assertEquals(expected, actual);
    }

    @Test
    void shouldNotCreateNull() {
        AppUser user = null;

        Result<AppUser> result = service.create(user);

        assertFalse(result.isSuccess());
        assertEquals(1, result.getMessages().size());
        assertTrue(result.getMessages().get(0).contains("cannot be null"));
    }

    @Test
    void shouldNotCreateNullUsername() {
        AppUser user = new AppUser(
                null,
                null,
                "P@ssw0rd!",
                false,
                "USER");

        Result<AppUser> result = service.create(user);

        assertFalse(result.isSuccess());
        assertEquals(1, result.getMessages().size());
        assertTrue(result.getMessages().get(0).contains("`username` is required"));
    }

    @Test
    void shouldNotCreateEmptyUsername() {
        AppUser user = new AppUser(
                null,
                "",
                "P@ssw0rd!",
                false,
                "USER");

        Result<AppUser> result = service.create(user);

        assertFalse(result.isSuccess());
        assertEquals(1, result.getMessages().size());
        assertTrue(result.getMessages().get(0).contains("`username` is required"));
    }

    @Test
    void shouldNotCreateDuplicateUsername() {
        AppUser expected = new AppUser(
                "983f1224-af4f-11eb-8368-0242ac110002",
                "john@smith.com",
                "$2a$10$ntB7CsRKQzuLoKY3rfoAQen5nNyiC/U60wBsWnnYrtQQi8Z3IZzQa",
                false,
                "ADMIN");

        when(repository.findByUsername(any())).thenReturn(expected);

        AppUser user = new AppUser(
                null,
                "john@smith.com",
                "P@ssw0rd!",
                false,
                "USER");

        Result<AppUser> result = service.create(user);

        assertFalse(result.isSuccess());
        assertEquals(1, result.getMessages().size());
        assertTrue(result.getMessages().get(0).contains("`username` is in use by an existing user"));
    }

    @Test
    void shouldNotCreateNullPassword() {
        AppUser user = new AppUser(
                null,
                "bob@jones.com",
                null,
                false,
                "USER");

        Result<AppUser> result = service.create(user);

        assertFalse(result.isSuccess());
        assertEquals(1, result.getMessages().size());
        assertTrue(result.getMessages().get(0).contains("`password` is required"));
    }

    @Test
    void shouldNotCreateEmptyPassword() {
        AppUser user = new AppUser(
                null,
                "bob@jones.com",
                "",
                false,
                "USER");

        Result<AppUser> result = service.create(user);

        assertFalse(result.isSuccess());
        assertEquals(2, result.getMessages().size());
        assertTrue(result.getMessages().get(0).contains("`password` is required") ||
                result.getMessages().get(1).contains("`password` is required"));
        assertTrue(result.getMessages().get(0).contains("`password` must be between 8 and 50 characters") ||
                result.getMessages().get(1).contains("`password` must be between 8 and 50 characters"));
    }

    @Test
    void shouldNotCreateShortPassword() {
        AppUser user = new AppUser(
                null,
                "bob@jones.com",
                "1234",
                false,
                "USER");

        Result<AppUser> result = service.create(user);

        assertFalse(result.isSuccess());
        assertEquals(1, result.getMessages().size());
        assertTrue(result.getMessages().get(0).contains("`password` must be between 8 and 50 characters"));
    }

    @Test
    void shouldNotCreatePasswordWithNoLetter() {
        AppUser user = new AppUser(
                null,
                "bob@jones.com",
                "12345678!",
                false,
                "USER");

        Result<AppUser> result = service.create(user);

        assertFalse(result.isSuccess());
        assertEquals(1, result.getMessages().size());
        assertTrue(result.getMessages().get(0).contains("`password` must be at least 8 characters and contain a digit, a letter, and a non-digit/non-letter"));
    }

    @Test
    void shouldNotCreatePasswordWithNoDigit() {
        AppUser user = new AppUser(
                null,
                "bob@jones.com",
                "P@ssword!",
                false,
                "USER");

        Result<AppUser> result = service.create(user);

        assertFalse(result.isSuccess());
        assertEquals(1, result.getMessages().size());
        assertTrue(result.getMessages().get(0).contains("`password` must be at least 8 characters and contain a digit, a letter, and a non-digit/non-letter"));
    }

    @Test
    void shouldNotCreatePasswordWithNoOther() {
        AppUser user = new AppUser(
                null,
                "bob@jones.com",
                "Passw0rd",
                false,
                "USER");

        Result<AppUser> result = service.create(user);

        assertFalse(result.isSuccess());
        assertEquals(1, result.getMessages().size());
        assertTrue(result.getMessages().get(0).contains("`password` must be at least 8 characters and contain a digit, a letter, and a non-digit/non-letter"));
    }

    @Test
    void shouldNotCreateWithId() {
        AppUser user = new AppUser(
                UUID.randomUUID().toString(),
                "bob@jones.com",
                "P@ssw0rd!",
                false,
                "USER");

        Result<AppUser> result = service.create(user);

        assertFalse(result.isSuccess());
        assertEquals(1, result.getMessages().size());
        assertTrue(result.getMessages().get(0).contains("`id` should not be set"));
    }

    @Test
    void shouldCreate() {
        String userId = UUID.randomUUID().toString();

        AppUser expected = new AppUser(
                userId,
                "bob@jones.com",
                null,
                false,
                "USER");

        AppUser user = new AppUser(
                null,
                "bob@jones.com",
                "P@ssw0rd!",
                false,
                "USER");

        when(repository.create(any())).thenReturn(user);
        when(uuidGenerator.getUUIDString()).thenReturn(userId);

        Result<AppUser> result = service.create(user);

        // HACK: Workaround to get the hashed password before comparing the expected and actual AppUser objects.
        expected.setPassword(user.getPassword());

        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals(expected, result.getPayload());
        assertTrue(encoder.matches("P@ssw0rd!", user.getPassword()));
    }
}