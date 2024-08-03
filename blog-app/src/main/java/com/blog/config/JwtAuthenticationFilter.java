package com.blog.config;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import org.slf4j.Logger;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private Logger logger = LoggerFactory.getLogger(OncePerRequestFilter.class);
    @Autowired
    private JWTHelper jwtHelper;

    @Autowired
    private UserDetailsService userDetailsService;
//    1. get token from request
//    2. validate token
//    3. get username from token
//    4. load user associated with this token
//    5. set authentication
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //Authorization
        String requestHeader = request.getHeader("Authorization");

        logger.info(" Header : {}",requestHeader);
        String username = null;
        String token = null;
        if(requestHeader != null && requestHeader.startsWith("Bearer")){
//            looking good
            token = requestHeader.substring(7);
            try{
                username = jwtHelper.getUserNamefromToken(token);
            }catch (IllegalArgumentException e){
                logger.info("Illegal Argument while fetching the username !!");
                e.printStackTrace();
            }catch (ExpiredJwtException e){
                logger.info("Given Jwt token is expired !!");
                e.printStackTrace();
            }catch (MalformedJwtException e){
                logger.info("Some Change has done in token !! invalid token");
                e.printStackTrace();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        else{
            logger.info("Invalid Header Value !!");
        }

        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){
            //fetch userdetails from username
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            Boolean validateToken = this.jwtHelper.validateToken(token,userDetails);
            if(validateToken){
                //set the authentication
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
            else {
                logger.info("Validation fails !!");
            }
        }
        filterChain.doFilter(request,response);
    }
}
