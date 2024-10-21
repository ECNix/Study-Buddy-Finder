package study.buddy.api.security;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
/*
import java.security.SignatureException;
*/
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;
@Service
public class JWToken {
    //TODO: we need to do something else with this rather than have it in source code probs...
    String secret = "hfWHfnou8pp47bqweUTAmj91h1H13ha2ezx23y5";

    Key hmacKey = new SecretKeySpec(Base64.getDecoder().decode(secret),
            SignatureAlgorithm.HS256.getJcaName());


    public String createToken(String username) {
        Instant now = Instant.now();
        String jwtToken = Jwts.builder()
                .claim("username", username)
                .setSubject(username)
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(1, ChronoUnit.MINUTES)))
                .signWith(SignatureAlgorithm.HS256, hmacKey)
                .compact();

        return jwtToken;
    }

    public String getSubject(String jwtString) {
        Key hmacKey = new SecretKeySpec(Base64.getDecoder().decode(secret),
                SignatureAlgorithm.HS256.getJcaName());
        Claims claimsJws = Jwts.parser()
                .setSigningKey(hmacKey)
                .parseClaimsJws(jwtString).getBody();
        return claimsJws.getSubject();
    }

    public Boolean validate(String jwtString){

        Key hmacKey = new SecretKeySpec(Base64.getDecoder().decode(secret),
                SignatureAlgorithm.HS256.getJcaName());

        try{
            //this one did not want to work, cuz parserBuilder said no.
            /*Jws<Claims> jwt = Jwts.parserBuilder()
                    .setSigningKey(hmacKey)
                    .build()
                    .parseClaimsJws(jwtString);*/

            //this one works tho :D
             Claims claimsJws = Jwts.parser()
                    .setSigningKey(hmacKey)
                    .parseClaimsJws(jwtString).getBody();

             Date exp = claimsJws.getExpiration();
             Date now = new Date();

            return exp.after(now);
        }catch(SignatureException se){
            return false;
        }

    }
}
