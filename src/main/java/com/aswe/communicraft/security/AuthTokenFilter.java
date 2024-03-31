package com.aswe.communicraft.security;

import io.micrometer.common.lang.NonNull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * The AuthTokenFilter class is a filter that processes and
 * validates authentication tokens, once per each request.
 */
@Configuration
public class AuthTokenFilter extends OncePerRequestFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthTokenFilter.class);

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        try {
            if (request.getServletPath().contains("/auth")) {
                filterChain.doFilter(request, response);
                return;
            }

            final String authHeader = request.getHeader("Authorization");
            final String jwt;

            if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }



            String username = jwtUtils.getUserNameFromJwtToken(authHeader);
            Integer userId = jwtUtils.getIdFromJwtToken(authHeader);
            String userRole = jwtUtils.getRoleFromJwtToken(authHeader);

            LOGGER.info("doFilterInternal :: got username = " + username + " with id = " + userId + " and role = " + userRole + " from jwt.");

            UserDetailsImpl userDetails = (UserDetailsImpl) jwtUtils.loadUserByUsername(username);
            List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(userRole));
            userDetails.setAuthorities(authorities);

            if(jwtUtils.validateJwtToken(authHeader)) {
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                LOGGER.warn("doFilterInternal :: Invalid token for the requested resource");
            }

        } catch (Exception e) {
            LOGGER.error("Cannot set user authentication: {}", e.getMessage());
        }

        filterChain.doFilter(request, response);
    }
}

