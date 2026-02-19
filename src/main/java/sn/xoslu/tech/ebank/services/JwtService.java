package sn.xoslu.tech.ebank.services;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.userdetails.UserDetails;
import sn.xoslu.tech.ebank.dtos.AuthResponse;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

public interface JwtService {
     String extractUsername(String token);
     Date extractExpiration(String token);
     <T> T extractClaim(String token, Function<Claims, T> claimsResolver);
     Boolean validateToken(String token, UserDetails userDetails);
     AuthResponse generateToken(String userName);
     Claims getAllClaims(String token);
     String getToken(HttpServletRequest request);
     boolean isBearer(HttpServletRequest request);
     boolean isTokenValid(String token,UserDetails userDetails);
}
