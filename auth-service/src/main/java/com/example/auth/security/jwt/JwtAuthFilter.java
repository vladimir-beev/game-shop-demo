package com.example.auth.security.jwt;

import com.example.auth.service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        //Checks if no Authorization header or not Bearer
        if (isMissingOrInvalidHeader(authHeader)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            final String jwt = authHeader.substring(7);
            final String email = jwtService.extractSubject(jwt);

            //If not authenticated yet
            if (isNotAuthenticated(email)) {
                UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);

                //Validate jwt
                if (jwtService.isTokenValid(jwt, userDetails)) {

                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
                else {
                    //If token invalid return 401
                    sendUnauthorizedError(response);
                    return;
                }
            }
        }
        catch (Exception e) { //Handle any validation error
            sendUnauthorizedError(response);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private static boolean isNotAuthenticated(String email) {
        return email != null && SecurityContextHolder.getContext().getAuthentication() == null;
    }

    private static boolean isMissingOrInvalidHeader(String authHeader) {
        return authHeader == null || !authHeader.startsWith("Bearer ");
    }

    private static void sendUnauthorizedError(@NonNull HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write("{\"error\": \"Invalid or expired token\"}");
    }

    //Skip /auth/** endpoints
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return path.startsWith("/auth/");
    }
}
