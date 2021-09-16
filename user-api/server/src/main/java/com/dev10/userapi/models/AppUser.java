package com.dev10.userapi.models;

import com.dev10.userapi.validation.PasswordComplexity;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class AppUser {
    private String id;
    @NotBlank(message = "`username` is required.")
    @Size(max = 255, message = "`username` cannot be longer than {max} characters.")
    private String username;
    @NotBlank(message = "`password` is required.")
    @Size(min = 8, max = 50, message = "`password` must be between {min} and {max} characters.")
    @PasswordComplexity(message = "`password` must be at least 8 characters and contain a digit, a letter, and a non-digit/non-letter.")
    private String password;
    private boolean disabled;
    private String firstName;
    private String lastName;
    private String emailAddress;
    private String mobilePhone;

    private List<String> roles = new ArrayList<>();

    public AppUser() {
    }

    public AppUser(String id, String username, String password, boolean disabled, String... roles) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.disabled = disabled;
        this.roles = Arrays.asList(roles);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @JsonProperty("first_name")
    public String getFirstName() {
        return firstName;
    }

    @JsonProperty("first_name")
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @JsonProperty("last_name")
    public String getLastName() {
        return lastName;
    }

    @JsonProperty("last_name")
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @JsonProperty("email_address")
    public String getEmailAddress() {
        return emailAddress;
    }

    @JsonProperty("email_address")
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    @JsonProperty("mobile_phone")
    public String getMobilePhone() {
        return mobilePhone;
    }

    @JsonProperty("mobile_phone")
    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public boolean hasRole(String role) {
        if (roles == null) {
            return false;
        }
        return roles.contains(role);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AppUser appUser = (AppUser) o;
        return disabled == appUser.disabled &&
                id.equals(appUser.id) &&
                username.equals(appUser.username) &&
                Objects.equals(password, appUser.password) &&
                Objects.equals(firstName, appUser.firstName) &&
                Objects.equals(lastName, appUser.lastName) &&
                Objects.equals(emailAddress, appUser.emailAddress) &&
                Objects.equals(mobilePhone, appUser.mobilePhone) &&
                Objects.equals(roles, appUser.roles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, disabled, firstName, lastName, emailAddress, mobilePhone, roles);
    }
}
