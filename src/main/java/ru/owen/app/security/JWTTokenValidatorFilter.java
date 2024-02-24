package ru.owen.app.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.owen.app.constants.ProjectConstants;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.logging.Logger;

public class JWTTokenValidatorFilter extends OncePerRequestFilter {
    Logger logger = Logger.getLogger(JWTTokenValidatorFilter.class.getName());

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String jwt = request.getHeader(ProjectConstants.JWT_HEADER);
        if (null != jwt) {
            try {
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(ProjectConstants.KEY)
                        .build()
                        .parseClaimsJws(jwt)
                        .getBody();

                String username = String.valueOf(claims.get(ProjectConstants.EMAIL));
                String role = String.valueOf(claims.get(ProjectConstants.ROLE));

                Authentication auth = new UsernamePasswordAuthenticationToken(username, null,
                        Collections.singleton(new SimpleGrantedAuthority(role)));
                SecurityContextHolder.getContext().setAuthentication(auth);
            } catch (Exception ex) {
                response.addHeader("Content-Type", "application/json;charset=UTF-8");
                response.getOutputStream().print(ex.getMessage());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                logger.info("Exception has been thrown. Something went wrong during jwt parsing.");
            }
        }
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return Arrays.stream(ProjectConstants.WHITE_LIST[0]).anyMatch(str -> request.getServletPath().matches(str));
//        return true;
    }
}
