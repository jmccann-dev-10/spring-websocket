package com.dev10.userapi.utility;

import com.dev10.userapi.models.AppUser;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.util.Date;

import static io.jsonwebtoken.SignatureAlgorithm.HS256;

@Component
public class JwtConverter {
    private final SecretKeySpec secretKeySpec;

    private final String ISSUER = "dev10-users-api";
    private final int EXPIRATION_MINUTES = 1440; // 24 hours
    private final int EXPIRATION_MILLIS = EXPIRATION_MINUTES * 60 * 1000;

    public JwtConverter(ApiProperties apiProperties) {
        secretKeySpec = new SecretKeySpec(
                apiProperties.getJwtSecretKey().getBytes(), HS256.getJcaName());
    }

    public String getTokenFromUser(AppUser user) {
        return Jwts.builder()
                .setIssuer(ISSUER)
                .setSubject(user.getUsername())
                .claim("id", user.getId())
                .claim("first_name", user.getFirstName())
                .claim("last_name", user.getLastName())
                .claim("email_address", user.getEmailAddress())
                .claim("mobile_phone", user.getMobilePhone())
                .claim("roles", String.join(",", user.getRoles()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_MILLIS))
                .signWith(secretKeySpec)
                .compact();
    }

    public AppUser getUserFromToken(String token) {
        if (token == null || token.isBlank()) {
            return null;
        }

        try {
            Jws<Claims> jws = Jwts.parserBuilder()
                    .requireIssuer(ISSUER)
                    .setSigningKey(secretKeySpec)
                    .build()
                    .parseClaimsJws(token);

            String username = jws.getBody().getSubject();

            Claims claims = jws.getBody();
            String id = (String)claims.get("id");
            String firstName = (String)claims.get("first_name");
            String lastName = (String)claims.get("last_name");
            String emailAddress = (String)claims.get("email_address");
            String mobilePhone = (String)claims.get("mobilePhone");
            String rolesString = (String)claims.get("roles");
            String[] roles = rolesString.split(",");

            AppUser appUser = new AppUser(id, username, null, false, roles);

            appUser.setFirstName(firstName);
            appUser.setLastName(lastName);
            appUser.setEmailAddress(emailAddress);
            appUser.setMobilePhone(mobilePhone);

            return appUser;

        } catch (JwtException e) {
            System.out.println(e);
        }

        return null;
    }
}