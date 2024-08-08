package org.example.task_management_.auth;

import io.jsonwebtoken.*;
import org.example.task_management_.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class JwtTokenProvider {

    @Value("${jwt.token.secret}")
    private String secretKey;

    @Value("${jwt.token.expired}")
    private long validityInMilliseconds;


    public String generateToken(Long id, String email, String password, String username, Set<User.Role> roles) {
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("id", id);
        claims.put("roles", getUserRoleNamesFromJWT(roles));
        claims.put("username", username);
        claims.put("email", email);
        claims.put("password", password);

        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        User userDetails = new User();
        userDetails.setId(getUserIdFromJWT(token));
        userDetails.setUsername(getUserUsernameFromJWT(token));
        userDetails.setEmail(getUserEmailFromJWT(token));
        userDetails.setPassword(getUserPasswordFromJWT(token));
        userDetails.setRoles(getUserRolesFromJWT(token));
        return new UsernamePasswordAuthenticationToken(userDetails, token, userDetails.getRoles());
    }

    public Set<User.Role> getUserRolesFromJWT(String token) {
        List<String> roles = (List<String>) Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().get("roles");
        return getUserRoleNamesFromJWT(roles);
    }

    private Set<User.Role> getUserRoleNamesFromJWT(List<String> roles) {
        Set<User.Role> result = new HashSet<>();
        roles.forEach(s -> result.add(User.Role.valueOf(s)));
        return result;
    }

    private Set<String> getUserRoleNamesFromJWT(Set<User.Role> roles) {
        Set<String> result = new HashSet<>();
        roles.forEach(role -> result.add(role.getAuthority()));
        return result;
    }

    public String getUserEmailFromJWT(String token) {
        return (String) Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().get("email");
    }

    public String getUserUsernameFromJWT(String token) {
        return (String) Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().get("username");
    }

    public Long getUserIdFromJWT(String token) {
        Integer i = (Integer) Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().get("id");
        return Long.valueOf(i);
    }

    public String getUserPasswordFromJWT(String token) {
        return (String) Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().get("password");
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);

            return !claims.getBody().getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}



