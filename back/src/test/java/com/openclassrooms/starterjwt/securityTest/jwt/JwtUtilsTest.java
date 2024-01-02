package com.openclassrooms.starterjwt.securityTest.jwt;

import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JwtUtilsTest {



    @Autowired
    private JwtUtils jwtUtils;

    @Test
    void generateJwtToken() {
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                new UserDetailsImpl(1L, "robin", "damian", "wayne", true, "123456" ), null);

        String jwtToken = jwtUtils.generateJwtToken(authentication);

        assertNotNull(jwtToken);
        assertTrue(jwtToken.length() > 0);
    }

   @Test
    void getUserNameFromJwtToken() {
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                new UserDetailsImpl(2L, "robin", "damian", "wayne", true, "123456" ), null);

        String jwtToken = jwtUtils.generateJwtToken(authentication);

        String usernameFromToken = jwtUtils.getUserNameFromJwtToken(jwtToken);

        assertEquals("robin", usernameFromToken);
    }


    @Test
    void validateJwtTokenInvalid() {
        assertFalse(jwtUtils.validateJwtToken("invalidToken"));
    }

}
