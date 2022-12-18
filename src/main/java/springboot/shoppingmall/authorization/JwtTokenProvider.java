package springboot.shoppingmall.authorization;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import springboot.shoppingmall.authorization.AuthorizedUser;
import springboot.shoppingmall.user.domain.User;

@Component
@Slf4j
public class JwtTokenProvider {
    private String secretKey = "tndusdlqkqhapfhd";

    private long tokenValidTime = 30 * 60 * 1000L;//30 * 60 * 1000L; // 30분

    // 객체 생성 후(Component 니까 bean 에 등록 후), PostConstruct 실행
    @PostConstruct
    public void encryptSecretKey(){
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    // jwt 토큰 생성
    public String createToken(User user) {
        Claims claims = Jwts.claims().setSubject(user.getId().toString());
        claims.put("role", "NORMAL");

        Date now = new Date();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(now.getTime() + tokenValidTime))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public Long getUserId(String token){
        String subject = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
        return Long.valueOf(subject);
    }

    // request 애서 jwt 토큰 추출
    public String resolveToken(HttpServletRequest request){
        return request.getHeader("X-AUTH-TOKEN");
    }

    // jwt 토큰 유효성 체크
    public boolean validateExpireToken(String jwtToken){
        try {
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
            return !claimsJws.getBody().getExpiration().before(new Date());
        }catch (Exception e){
            return false;
        }
    }

    public long getExpiration(String jwtToken){
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
        return claimsJws.getBody().getExpiration().getTime();
    }
}
