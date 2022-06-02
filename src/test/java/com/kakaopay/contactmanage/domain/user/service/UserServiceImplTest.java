package com.kakaopay.contactmanage.domain.user.service;

import com.kakaopay.contactmanage.domain.user.entity.User;
import com.kakaopay.contactmanage.domain.user.repository.UserRepository;
import com.kakaopay.contactmanage.help.exception.DuplicatedEmailAddrException;
import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.servlet.http.HttpServletRequest;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private HttpServletRequest request;
    private PasswordEncoder passwordEncoder;
    private UserService userService;
    private User.SignUpDto signUpDto;

    @BeforeEach
    void 초기설정(){
        passwordEncoder = new BCryptPasswordEncoder();
        userService = new UserServiceImpl(userRepository, request, passwordEncoder);

        signUpDto = new User.SignUpDto();
        signUpDto.setUserNm("두승현");
        signUpDto.setPassword("qwer1234!@");
        signUpDto.setUserEmailAddr("doosh17@naver.com");
    }

    @Test
    void signup_중복가입오류(){
        String duplicatedEmail ="dup@dup.com";
        signUpDto.setUserEmailAddr(duplicatedEmail);

        BDDMockito.given(userRepository.existsByUserEmailAddr(duplicatedEmail))
                .willReturn(true);

        try {
            userService.signUp(signUpDto);
            fail();
        } catch (Exception e) {
            assertThat(e.getCause() instanceof DuplicatedEmailAddrException);
        }
    }

    @Test
    void signup(){

        BDDMockito.given(userRepository.existsByUserEmailAddr(BDDMockito.any()))
                .willReturn(false);

        User.OutDto dto = userService.signUp(signUpDto);

        assertThat(dto.getUserNm()).isEqualTo(signUpDto.getUserNm());
        assertThat(dto.getUserEmailAddr()).isEqualTo(signUpDto.getUserEmailAddr());
    }

    @Test
    void 패스워드암호화_테스트(){
        UserServiceImpl userServiceImpl = (UserServiceImpl) userService;
        String encrypted = userServiceImpl.encryptPassword(signUpDto.getPassword());

        assertThat(passwordEncoder.matches(signUpDto.getPassword(),encrypted));
    }
}