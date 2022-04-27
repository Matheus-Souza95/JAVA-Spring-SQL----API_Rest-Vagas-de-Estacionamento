package com.api.parkingcontrol.security;

import com.api.parkingcontrol.models.UserModel;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TokenService {

    //pegando valor do application.properties
    @Value("${parking-control.jwt.timeSessionExpiration}")
    private String timeSessionExpiration;

    @Value("${parking-control.jwt.secret}")
    private String secret;

    public String createToken(Authentication authentication) {

        UserModel userLogged = (UserModel) authentication.getPrincipal();
        Date dateNow = new Date();
        Date dateExpiration = new Date(dateNow.getTime() + Long.parseLong(timeSessionExpiration));

        return Jwts.builder().
                setIssuer("API").
                setSubject(userLogged.getId().toString()).
                setIssuedAt(dateNow).
                setExpiration(dateExpiration).
                signWith(SignatureAlgorithm.HS256, secret).
                compact();
    }

    public boolean isValidToken(String token) {
        try {
            Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Long getUserId(String token) {
        Claims claims = Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token).getBody();
        return Long.parseLong(claims.getSubject());
    }
}
