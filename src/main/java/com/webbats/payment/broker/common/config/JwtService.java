package com.webbats.payment.broker.common.config;

import com.webbats.payment.broker.common.constants.AppConstants;
import com.webbats.payment.broker.common.entity.User;
import com.webbats.payment.broker.common.entity.ValidToken;
import com.webbats.payment.broker.common.repo.RefreshTokenRepository;
import com.webbats.payment.broker.common.repo.ValidTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final ValidTokenRepository validTokenRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;

    @Value("${allow.concurrent.login:true}")
    private boolean allowConcurrentLogin;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String extractUserType(String token) {
        return extractClaim(token, claims -> claims.get("type", String.class));
    }

    public String generateToken(User userDetails) {
        List<SimpleGrantedAuthority> userGrants = (List<SimpleGrantedAuthority>) userDetails.getAuthorities();
        Set<String> roles = userGrants.stream()
                .map(GrantedAuthority::getAuthority)
                .filter(auth -> auth.startsWith(AppConstants.ROLE_PREFIX))
                .collect(Collectors.toSet());
        Set<String> otherAuthorities = userGrants.stream()
                .map(GrantedAuthority::getAuthority)
                .filter(auth -> !auth.startsWith(AppConstants.ROLE_PREFIX))
                .collect(Collectors.toSet());
        Map<String, Object> claims = new HashMap<>();
        claims.put("type", userDetails.getUserType());
        claims.put("roles", roles);
        claims.put("authorities", otherAuthorities);
        return generateToken(claims, userDetails);
    }

    public List<SimpleGrantedAuthority> extractAuthoritiesAndRoles(String token) {
        Claims claims = extractAllClaims(token);

        // Extract roles and authorities from the token
        List<String> roles = claims.get("roles", List.class);
        List<String> authorities = claims.get("authorities", List.class);

        // Combine roles and authorities into a single list
        List<String> combined = Stream.concat(
                roles != null ? roles.stream() : Stream.empty(),
                authorities != null ? authorities.stream() : Stream.empty()
        ).toList();

        // Convert to List<SimpleGrantedAuthority> and return
        return combined.stream()
                .map(SimpleGrantedAuthority::new)
                .toList();
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSignInKey())
                .compact();
    }

    public String generateTokenForService(Map<String, Object> extraClaims, String clientId) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(clientId)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSignInKey())
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails, String deviceFingerprint) {
        final String userName = extractUsername(token);
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token) && validTokenFromDB(token, userName, deviceFingerprint));
    }

    public Boolean validTokenFromDB(String token, String email, String deviceFingerprint) {
        if (!allowConcurrentLogin)
            return validTokenRepository.countByTokenAndUserEmailAAndDeviceFingerprint(token, email, deviceFingerprint) > 0;

        List<ValidToken> validTokens = validTokenRepository.findByToken(token);
        return !validTokens.isEmpty();
    }

    @Transactional
    public void saveTokenToDB(User user, String token, String deviceFingerprint) {
        ValidToken validToken = new ValidToken();
        validToken.setToken(token);
        validToken.setUser(user);
        validToken.setCreatedAt(new Date());
        if (!allowConcurrentLogin) {
            validTokenRepository.deleteByUserId(user.getId());
            refreshTokenRepository.deleteByUserId(user.getId());
            validToken.setDeviceFingerprint(deviceFingerprint);
        } else {
            validTokenRepository.updateFingerprintByUserId(user.getId(), null);
        }
        validTokenRepository.save(validToken);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
