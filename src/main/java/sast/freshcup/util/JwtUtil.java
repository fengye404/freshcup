package sast.freshcup.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import sast.freshcup.common.constants.RedisKeyConst;
import sast.freshcup.common.enums.ErrorEnum;
import sast.freshcup.entity.Account;
import sast.freshcup.exception.LocalRunTimeException;
import sast.freshcup.service.RedisService;

import java.util.Calendar;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author: 風楪fy
 * @create: 2022-01-19 16:18
 **/
@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private int expiration;

    private final RedisService redisService;

    public JwtUtil(RedisService redisService) {
        this.redisService = redisService;
    }

    public String generateToken(Account account) {
        Calendar time = Calendar.getInstance();
        time.add(Calendar.HOUR, expiration);
        JWTCreator.Builder builder = JWT.create();
        builder.withClaim("uid", account.getUid());
        builder.withClaim("username", account.getUsername());
        builder.withClaim("role", account.getRole());
        builder.withExpiresAt(time.getTime());
        return builder.sign(Algorithm.HMAC256(secret));
    }

    public Map<String, Claim> getClaims(String token) {
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret)).build();
            DecodedJWT decodedJWT = verifier.verify(token);
            return decodedJWT.getClaims();
        } catch (JWTVerificationException e) {
            e.printStackTrace();
            throw new LocalRunTimeException(ErrorEnum.TOKEN_ERROR);
        }
    }

    public Account getAccount(String token) {
        Map<String, Claim> claims = this.getClaims(token);
        Account account = new Account();
        account.setUid(claims.get("uid").asLong());
        account.setUsername(claims.get("username").asString());
        account.setRole(claims.get("role").asInt());
        return account;
    }

    /**
     * 判断一个account在redis中的token是否过期
     *
     * @param account
     */
    public Boolean isExpired(Account account) {
        Long expire = redisService.getExpire(RedisKeyConst.getTokenKey(account));
        return expire <= 0;
    }

    public void reFreshToken(Account account) {
        redisService.expire(RedisKeyConst.getTokenKey(account), 30, TimeUnit.DAYS);
    }
}
