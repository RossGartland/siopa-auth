package com.siopa.siopa_auth.security.jwt;


import java.io.IOException;

import com.siopa.siopa_auth.security.services.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
/**
 * Represents an auth filter by extending the OncePerRequestFilter class.
 */
public class AuthTokenFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

    /**
     * Overrides the doFilterInternal method provided by the OncePerRequestFilter class.
     * Executes only 1 timer per request to service.
     * @param request
     * @param response
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String jwt = parseJwt(request); //Parse the provided JWT.
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) { //Executes if JWT is provided and is valid.
                String username = jwtUtils.getUserNameFromJwtToken(jwt); //Gets username from the JWT.
                UserDetails userDetails = userDetailsService.loadUserByUsername(username); //Returns user details by providing the username.
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
                        userDetails.getAuthorities()); //Authenticates username and password.
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            logger.error("Cannot set user authentication: {}", e);
        }
        filterChain.doFilter(request, response);
    }

    /**
     * Retrieve and parse JWT from the auth header.
     * @param request
     * @return
     */
    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization"); //Get header.
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7, headerAuth.length()); //Return parsed JWT.
        }
        return null; //Returns null if no JWT in header.
    }
}