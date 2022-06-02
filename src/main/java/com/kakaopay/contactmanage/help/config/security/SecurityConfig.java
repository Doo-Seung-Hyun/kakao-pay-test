package com.kakaopay.contactmanage.help.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakaopay.contactmanage.domain.user.entity.User;
import com.kakaopay.contactmanage.domain.user.service.LoginService;
import com.kakaopay.contactmanage.help.exception.LoginFailureException;
import com.kakaopay.contactmanage.help.globalDto.ErrorResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.Filter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final LoginService loginService;

    @Autowired
    public SecurityConfig(LoginService loginService) {
        this.loginService = loginService;
    }


    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/h2-console/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //api 기반 로그인이므로 csrf, formLogin disable
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/users/signup", "/users/signin").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/users/signin")
                .usernameParameter("userEmailAddr")
                .passwordParameter("password")
                .successHandler(new AuthenticationSuccessHandler() {
                    @Override
                    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                        User currentUser = ((User) authentication.getPrincipal());

                        response.setContentType("application/json;charset=UTF-8");
                        response.setStatus(HttpStatus.OK.value());
                        response.getWriter().write(new ObjectMapper().writeValueAsString(currentUser.getOutDto()));
                    }
                })
                .failureHandler(new AuthenticationFailureHandler() {
                    @Override
                    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
                        ErrorResponseDto<User.SignInDto> errorResponseDto
                                = new ErrorResponseDto<>(request.getRequestURI(), null, "로그인을 실패했습니다. 아이디/비밀번호를 확인해주세요.");

                        response.setContentType("application/json;charset=UTF-8");
                        response.setStatus(HttpStatus.UNAUTHORIZED.value());
                        response.getWriter().write(errorResponseDto.toJsonString());
                    }
                })
                .and()
                .httpBasic().authenticationEntryPoint(new AuthenticationEntryPoint() {
            @Override
            public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
                ErrorResponseDto<User.SignInDto> errorResponseDto
                        = new ErrorResponseDto<>(request.getRequestURI(), null, "미인증 접속 시도입니다. 로그인 후 다시 시도해 주세요.");
                response.setContentType("application/json;charset=UTF-8");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write(errorResponseDto.toJsonString());
            }
        })
        ;
                
            //ExtendedUsernamePasswordAuthenticationFilter filter 등록
//            .addFilterBefore(getCustomFilter(), UsernamePasswordAuthenticationFilter.class);

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(loginService)
                .passwordEncoder(passwordEncoder());
    }

    /**
     * 로그인 인증에 사용되는 userDetailsService를 
     * 실제 로그인 service 객체인 userService로 설정 
     */
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(loginService);
        return new ProviderManager(provider);
    }

    /**
     * JSON 로그인이 가능하도록 확장한 ExtendedUsernamePasswordAuthenticationFilter
     * 를 Filter로 등록
     */
    private Filter getCustomFilter() throws Exception {
        ExtendedUsernamePasswordAuthenticationFilter authenticationFilter = new ExtendedUsernamePasswordAuthenticationFilter();
        authenticationFilter.setFilterProcessesUrl("/users/signin");
        authenticationFilter.setAuthenticationManager(authenticationManager());

        return authenticationFilter;
    }

    /**
     * PasswordEncoder를 BCryptPasswordEncoder로 설정
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
