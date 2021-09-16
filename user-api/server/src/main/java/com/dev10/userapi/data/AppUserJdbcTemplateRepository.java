package com.dev10.userapi.data;

import com.dev10.userapi.models.AppUser;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.UUID;

@Repository
public class AppUserJdbcTemplateRepository implements AppUserRepository {
    private final JdbcTemplate jdbcTemplate;

    public AppUserJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @Transactional
    public AppUser findByUsername(String username) {
        final String sql = "select app_user_id, username, password_hash, disabled, "
                + "first_name, last_name, email_address, mobile_phone "
                + "from app_user "
                + "where username = ?;";

        AppUser user = jdbcTemplate.query(sql, this::map, username).stream().findFirst().orElse(null);

        if (user != null) {
            user.setRoles(getRolesByUserId(user.getId()));
        }

        return user;
    }

    @Override
    @Transactional
    public AppUser create(AppUser user) {
        jdbcTemplate.update("insert into app_user (app_user_id, username, password_hash, " +
                        "first_name, last_name, email_address, mobile_phone) values (?, ?, ?, ?, ?, ?, ?);",
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmailAddress(),
                user.getMobilePhone());

        updateRoles(user);

        return user;
    }

    private void updateRoles(AppUser user) {
        // delete all roles, then re-add
        jdbcTemplate.update("delete from app_user_role where app_user_id = ?;", user.getId());

        if (user.getRoles() == null) {
            return;
        }

        for (String roleName : user.getRoles()) {
            String sql = "insert into app_user_role (app_user_id, app_role_id) "
                    + "select ?, app_role_id from app_role where app_role_name = ?;";

            jdbcTemplate.update(sql, user.getId(), roleName);
        }
    }

    private AppUser map(ResultSet rs, int rowId) throws SQLException {
        AppUser user = new AppUser();
        user.setId(rs.getString("app_user_id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password_hash"));
        user.setDisabled(rs.getBoolean("disabled"));
        user.setFirstName(rs.getString("first_name"));
        user.setLastName(rs.getString("last_name"));
        user.setEmailAddress(rs.getString("email_address"));
        user.setMobilePhone(rs.getString("mobile_phone"));
        return user;
    }

    private List<String> getRolesByUserId(String userId) {
        final String sql = "select r.app_role_name name "
                + "from app_user_role ur "
                + "inner join app_role r on ur.app_role_id = r.app_role_id "
                + "where ur.app_user_id = ?";

        return jdbcTemplate.query(sql, (rs, rowId) -> rs.getString("name"), userId);
    }
}
