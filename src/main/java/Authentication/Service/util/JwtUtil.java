package Authentication.Service.util;

import Authentication.Service.exception.TokenExpiredException;
import Authentication.Service.model.dto.response.AuthenticationResponse;
import Authentication.Service.service.CustomUserDetailsImpl;
import Authentication.Service.service.UserDetailsServiceImpl;
import Authentication.Service.service.impl.CustomUserDetails;
import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.DefaultClaims;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtUtil {

    @Value("${thunderstore.jwt.secret-key}")
    private String SECRET_KEY;

    @Value("${thunderstore.jwt.duration}")
    private long TOKEN_VALID_DURATION;

    @Value("${thunderstore.jwt.refreshExpirationDateInMs}")
    private long REFRESH_TOKEN_VALID_DURATION;


    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public int extractUserId(String token) {
        Map<String, Object> expectedMap = getMapFromIoJsonwebtokenClaims((DefaultClaims) extractAllClaims(token));
        return Integer.parseInt(expectedMap.get("user_id").toString());
    }

    public int extractOrganizationId(String token) {
        Map<String, Object> expectedMap = getMapFromIoJsonwebtokenClaims((DefaultClaims) extractAllClaims(token));
        return Integer.parseInt(expectedMap.get("org_id").toString());
    }

    public String generateToken(CustomUserDetailsImpl userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("user_id", userDetails.getId());
        claims.put("org_id", userDetails.getOrgId());
        return createToken(claims, userDetails.getEmail());
    }

    public String generateRefreshToken(CustomUserDetailsImpl userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("user_id", userDetails.getId());
        claims.put("org_id", userDetails.getOrgId());
        return createRefreshToken(claims, userDetails.getEmail());
    }

    public Boolean isValidToken(String token, CustomUserDetails userDetails) {
        final String email = extractEmail(token);
        return (email.equals(userDetails.getEmail()) && !isTokenExpired(token));
    }

    @SneakyThrows
    public boolean isValidToken(String authToken) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException ex) {
            throw new BadCredentialsException("INVALID_CREDENTIALS", ex);
        } catch (ExpiredJwtException ex) {
            throw TokenExpiredException.builder()
                    .message("The token has expired.")
                    .build();
        }
    }

    public AuthenticationResponse generateAuthenticationResponse(String jwt, UserDetailsServiceImpl userDetailsService) {
        CustomUserDetailsImpl customUserDetailsImpl = (CustomUserDetailsImpl) userDetailsService
                .loadUserByEmail(this.extractEmail(jwt));
        return AuthenticationResponse.builder()
                .access_token(this.generateToken(customUserDetailsImpl))
                .refresh_token(this.generateRefreshToken(customUserDetailsImpl))
                .build();
    }

    private String createRefreshToken(Map<String, Object> claims, String subject) {

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_VALID_DURATION))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();

    }

    private String createToken(Map<String, Object> claims, String subject) {

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_VALID_DURATION))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Map<String, Object> getMapFromIoJsonwebtokenClaims(DefaultClaims claims) {
        Map<String, Object> expectedMap = new HashMap<String, Object>();
        for (Map.Entry<String, Object> entry : claims.entrySet()) {
            expectedMap.put(entry.getKey(), entry.getValue());
        }
        return expectedMap;

    }

}
