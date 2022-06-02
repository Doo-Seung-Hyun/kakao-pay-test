package com.kakaopay.contactmanage.domain.user.service;

import com.kakaopay.contactmanage.domain.user.entity.User;
import com.kakaopay.contactmanage.domain.user.repository.UserRepository;
import com.kakaopay.contactmanage.help.exception.LoginFailureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.stream.Collectors;

@Service
public class LoginService implements UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    public LoginService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUserEmailAddr(username)
                .orElseThrow(()->new UsernameNotFoundException("로그인에 실패했습니다. 아이디/비밀번호를 확인해주세요."));
        user.setAuthorities(
            Arrays.asList("USER").stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList())
        );

        return user;
    }

}
