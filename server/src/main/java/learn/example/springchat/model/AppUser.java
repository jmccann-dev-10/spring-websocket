package learn.example.springchat.model;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class AppUser implements Principal {
    private String id;
    private String name;
    private List<String> roles = new ArrayList<>();

    public AppUser() {
    }

    public AppUser(String id, String username, String... roles) {
        this.id = id;
        this.name = username;
        this.roles = Arrays.asList(roles);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String username) {
        this.name = username;
    }

    @Override
    public String getName() {
        return name;
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
        return Objects.equals(id, appUser.id) && Objects.equals(name, appUser.name) && Objects.equals(roles, appUser.roles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, roles);
    }

}