package com.haihua.hhplms.security.auth.jwt.verifier;


public interface TokenVerifier {
    boolean verify(String jti);
}
