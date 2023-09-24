package org.example.configuration.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.example.entities.UserEntity;
import org.example.repositories.UserRoleRepository;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtService {
    private final UserRoleRepository userRoleRepository;
    //Ключ для шифрування токена
    private final String jwtSecret = "404E635266556A586E32734AFBBEEE83458743592357538782F413F4428472B4B6250645367566B5970";
    //Хто видає токен
    private final String jwtIssuer = "vova";

    public String generateAccessToken(UserEntity user) {
        var roles = userRoleRepository.findByUser(user);
        String [] rolesArray = roles.stream().map(r->r.getRole().getName()).toArray(String []:: new);
        return Jwts.builder()
                .setSubject(String.format("%s", user.getId()))
                .claim("email", user.getEmail())
                .claim("roles", rolesArray)
                .setIssuer(jwtIssuer)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+7*24*60*60*1000)) //1 тиждень
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();

    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // з токена можна витягнути Id юзера
    public String getUserId(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)    // перевіряється чи цей токен видавався нашим серваком
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject().split(",")[0]; //з токена бере перший елемент Id
    }
    // з токена можна витягнути username юзера
    public String getUsername(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();

        return claims.get("email").toString();
    }
    // метод повертає дату до якої живе токен
    public Date getExpirationDate(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();

        return claims.getExpiration();
    }
    //перевфряє чи наш токен валідний і чи видавався нашим сервером
    public boolean validate(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        } catch (SignatureException ex) {
            System.out.println("Invalid JWT signature - "+ ex.getMessage());
        } catch (MalformedJwtException ex) {
            System.out.println("Invalid JWT token - " + ex.getMessage());
        } catch (ExpiredJwtException ex) {
            System.out.println("Expired JWT token - " + ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            System.out.println("Unsupported JWT token - " + ex.getMessage());
        } catch (IllegalArgumentException ex) {
            System.out.println("JWT claims string is empty - " + ex.getMessage());
        }
        return false;
    }
}
