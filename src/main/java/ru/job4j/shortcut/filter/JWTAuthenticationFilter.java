package ru.job4j.shortcut.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.job4j.shortcut.model.LoginDTO;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

/**
 * Class is a JWT authentication filter.
 */
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    /**
     * Secret key.
     */
    public static final String SECRET = "SomeSecretKey";
    /**
     * Expired time 10 days.
     */
    public static final long EXPIRATION_TIME = 864_000_000;
    /**
     * Token prefix.
     */
    public static final String TOKEN_PREFIX = "Bearer ";
    /**
     * Header name.
     */
    public static final String HEADER_STRING = "Authorization";
    /**
     * Sign Up URL.
     */
    public static final String SIGN_UP_URL = "/site/registration";
    public static final String REDIRECT_URL = "/link/redirect";

    public AuthenticationManager authenticationManager;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    /**
     * Метод осуществляет проверку логина и пароля.
     *
     * @param request  запрос
     * @param response ответ
     */
    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest request,
            HttpServletResponse response) throws AuthenticationException {
        try {
            LoginDTO creds = new ObjectMapper().readValue(
                    request.getInputStream(), LoginDTO.class
            );
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            creds.getUsername(),
                            creds.getPassword(),
                            new ArrayList<>()
                    )
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Метод генерирует токен пользователя для аутентификации.
     *
     * @param request  запрос
     * @param response ответ
     * @param chain    представление о цепочке вызовов фильтров
     * @param auth     данные аутентифицированного пользователя
     */
    @Override
    protected void successfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain,
            Authentication auth
    ) {
        String token = JWT.create()
                .withSubject(((User) auth.getPrincipal()).getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(SECRET.getBytes()));
        response.addHeader(HEADER_STRING, TOKEN_PREFIX + token);
    }

    public static String createToken(String username) {
        return JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(SECRET.getBytes()));
    }
}
