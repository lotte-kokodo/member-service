package shop.kokodo.memberservice.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenCreator {

    @Value("${token.member.access.exp}")
    public Long ACCESS_TOKEN_EXP_TIME;

    @Value("${token.member.refresh.exp}")
    public Long REFRESH_TOKEN_EXP_TIME;

    @Value("${token.member.secret}")
    private String jwtTokenSecret;

    public String generateAccessToken(Long memberId) {
        Map<String, Object> claims = new HashMap<>();

        return Jwts.builder().setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setSubject(Long.toString(memberId))
                .signWith(SignatureAlgorithm.HS512, jwtTokenSecret)
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXP_TIME)).compact();
    }

    public String generateRefreshToken(Long memberId) {
        Map<String, Object> claims = new HashMap<>();

        return Jwts.builder().setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setSubject(Long.toString(memberId))
                .signWith(SignatureAlgorithm.HS512, jwtTokenSecret)
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXP_TIME)).compact();
    }
}
