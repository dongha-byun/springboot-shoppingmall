package springboot.shoppingmall.authorization.service;

import static springboot.shoppingmall.authorization.AuthorizationConstants.CLAIM_ACCESS_IP;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Slf4j
@RequiredArgsConstructor
@Component
public class JwtTokenProvider {
    private final JwtTokenExpireDurationStrategy expireDateStrategy;
    //private String secretKey = "secret_key_of_dong_ha_do_not_snap_this";
    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // jwt 토큰 생성
    public String createAccessToken(Long userId, String accessIp) {
        Claims claims = Jwts.claims().setSubject(userId.toString());
        claims.put(CLAIM_ACCESS_IP, accessIp);

        long accessTokenValidTime = expireDateStrategy.getAccessTokenExpireDuration(); // 30 * 60 * 1000L; // 30분
        return generateToken(claims, accessTokenValidTime);
    }

    public String createRefreshToken(Long userId, String accessIp){
        Claims claims = Jwts.claims().setSubject(userId.toString());
        claims.put(CLAIM_ACCESS_IP, accessIp);

        long refreshTokenValidTime = expireDateStrategy.getRefreshTokenExpireDuration(); // 14 * 24 * 60 * 60 * 1000L // 14일
        return generateToken(claims, refreshTokenValidTime);
    }

    private String generateToken(Claims claims, long accessTokenValidTime) {
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(now.getTime() + accessTokenValidTime))
                .signWith(key)
                .compact();
    }

    public Long getUserId(String token){
        String subject = getBodyOfToken(token).getSubject();
        return Long.valueOf(subject);
    }

    // jwt 토큰 유효성 체크
    public boolean validateExpireToken(String jwtToken){
        try {
            Claims claims = getBodyOfToken(jwtToken);
            return !claims.getExpiration().before(new Date());
        }catch (Exception e){
            return false;
        }
    }

    public String createExpireToken(){
        return Jwts.builder()
                .setExpiration(new Date())
                .signWith(key)
                .compact();
    }

    public boolean validateIpToken(String accessToken, String accessIp) {
        Claims claims = getBodyOfToken(accessToken);
        String tokenIp = (String) claims.get(CLAIM_ACCESS_IP);

        return accessIp.equals(tokenIp);
    }

    private Claims getBodyOfToken(String accessToken) {
        return Jwts.parserBuilder()
                .setSigningKey(key).build()
                . parseClaimsJws(accessToken).getBody();
    }
}
