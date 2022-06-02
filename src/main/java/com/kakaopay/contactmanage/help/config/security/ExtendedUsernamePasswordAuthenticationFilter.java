package com.kakaopay.contactmanage.help.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakaopay.contactmanage.domain.user.entity.User;
import com.kakaopay.contactmanage.help.globalDto.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;

/**
 * application/json 로그인 지원을 위한 UsernamePasswordAuthenticationFilter 클래스 확장
 */
public class ExtendedUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        //json인 경우
        if(request.getContentType().equals(MediaType.APPLICATION_JSON_VALUE)){
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                String jsonString = request.getReader().lines().collect(Collectors.joining());
                User.SignInDto signInDto = objectMapper.readValue(jsonString, User.SignInDto.class);
                UsernamePasswordAuthenticationToken authRequest =
                        new UsernamePasswordAuthenticationToken(signInDto.getUserEmailAddr(), signInDto.getPassword());
                request.setAttribute("userEmailAddr", signInDto.getUserEmailAddr());
                this.setDetails(request, authRequest);
                return this.getAuthenticationManager().authenticate(authRequest);
            } catch (IOException e) {
                logger.info(e.getMessage());
                throw new AuthenticationServiceException(e.getMessage(),e);
            }
        }else{ //json이 아닌 경우는 기존대로 처리
            return super.attemptAuthentication(request,response);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        User currentUser = ((User) authResult.getPrincipal());

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpStatus.OK.value());
        response.getWriter().write(new ObjectMapper().writeValueAsString(currentUser.getOutDto()));
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        User.SignInDto signInDto = new User.SignInDto();
        signInDto.setUserEmailAddr((String)request.getAttribute("userEmailAddr"));
        ErrorResponseDto<User.SignInDto> errorResponseDto
                = new ErrorResponseDto<>(request.getRequestURI(), signInDto, "로그인을 실패했습니다. 아이디/비밀번호를 확인해주세요.");

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.getWriter().write(errorResponseDto.toJsonString());
    }
}
