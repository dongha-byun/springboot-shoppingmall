package springboot.shoppingmall.authorization.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import springboot.shoppingmall.authorization.exception.ExpireTokenException;
import springboot.shoppingmall.user.domain.User;


@Slf4j
@RequiredArgsConstructor
@Component
public class JwtTokenProvider {

    private final JwtTokenExpireDurationStrategy expireDateStrategy;
    //private String secretKey = "secret_key_of_dong_ha_do_not_snap_this";
    private Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // jwt 토큰 생성
    public String createAccessToken(User user, String accessIp) {
        Date now = new Date();

        long accessTokenValidTime = expireDateStrategy.getAccessTokenExpireDuration(); // 30 * 60 * 1000L; // 30분
        return Jwts.builder()
                .setSubject(String.valueOf(user.getId()))
                .setIssuedAt(new Date())
                .setExpiration(new Date(now.getTime() + accessTokenValidTime))
                .signWith(key)
                .compact();
    }

    public String createRefreshToken(User user, String accessIp){
        Claims claims = Jwts.claims().setSubject(user.getId().toString());
        claims.put("access_ip", accessIp);

        Date now = new Date();

        long refreshTokenValidTime = expireDateStrategy.getRefreshTokenExpireDuration(); // 14 * 24 * 60 * 60 * 1000L // 14일
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(now.getTime() + refreshTokenValidTime))
                .signWith(key)
                .compact();
    }

    public Long getUserId(String token){
        if(!validateExpireToken(token)){
            throw new ExpireTokenException("토큰이 만료되었습니다.");
        }
        String subject = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
        return Long.valueOf(subject);
    }

    // jwt 토큰 유효성 체크
    public boolean validateExpireToken(String jwtToken){
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key).build()
                    .parseClaimsJws(jwtToken).getBody();
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
}
