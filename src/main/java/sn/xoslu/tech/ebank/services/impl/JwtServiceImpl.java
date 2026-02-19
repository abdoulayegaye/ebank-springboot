package sn.xoslu.tech.ebank.services.impl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import sn.xoslu.tech.ebank.dtos.AuthResponse;
import sn.xoslu.tech.ebank.services.JwtService;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class JwtServiceImpl implements JwtService {

    private static final String BEARER = "Bearer ";
    private static final String AUTHORIZATION = "Authorization";
    public static final String SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";

    @Autowired
    private UserInfoUserDetailsService userDetailsService;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public AuthResponse generateToken(String userName) {
        Map<String,Object> claims = new HashMap<>();
        List<String> roles = userDetailsService.loadUserByUsername(userName).getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        claims.put("roles", roles);
        String token = createToken(claims, userName);
        AuthResponse response = new AuthResponse();
        response.setToken(token);
        response.setRoles(roles.toString());
        response.setUsername(extractUsername(token));
        return response;
    }

    public String getToken(HttpServletRequest request){
        String tokenHeader = request.getHeader(AUTHORIZATION);
        if(tokenHeader != null){
            return tokenHeader.substring(BEARER.length());
        }
        return null;
    }

    public Claims getAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignKey())  // SECRETE HS256
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean isBearer(HttpServletRequest request){
        String tokenHeader = request.getHeader(AUTHORIZATION);
        if(tokenHeader != null){
            return tokenHeader.startsWith(BEARER);
        }
        return false;
    }

    public boolean isTokenValid(String token, UserDetails userDetails){
        return extractUsername(token).equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private String createToken(Map<String, Object> claims, String userName) {
        return Jwts
                .builder()
                .setClaims(claims)
                .setSubject(userName)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*30))
                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
    }

    private Key getSignKey() {
        byte[] keyBytes= Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
}
