package com.haihua.hhplms.security.model.token;

import com.haihua.hhplms.security.exception.JwtBadTokenException;
import com.haihua.hhplms.security.exception.JwtExpiredTokenException;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RawAccessJwtToken implements JwtToken {
    private static Logger logger = LoggerFactory.getLogger(RawAccessJwtToken.class);

    private String token;

    public RawAccessJwtToken(String token) {
        this.token = token;
    }

    /**
     * Parses and validates JWT Token signature.
     *
     * @throws JwtBadTokenException
     * @throws JwtExpiredTokenException
     *
     */
    public Jws<Claims> parseClaims(String signingKey) {
        try {
            return Jwts.parser().setSigningKey(signingKey).parseClaimsJws(this.token);
        } catch (UnsupportedJwtException | MalformedJwtException | IllegalArgumentException | SignatureException ex) {
            logger.error("Token无效", ex);
            throw new JwtBadTokenException(this, "Token无效", ex);
        } catch (ExpiredJwtException expiredEx) {
            logger.info("Token已过期", expiredEx);
            throw new JwtExpiredTokenException(this, "Token已过期", expiredEx);
        }
    }

    @Override
    public String getToken() {
        return token;
    }
}
