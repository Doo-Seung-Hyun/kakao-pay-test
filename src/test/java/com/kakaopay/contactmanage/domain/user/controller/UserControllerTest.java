package com.kakaopay.contactmanage.domain.user.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakaopay.contactmanage.domain.user.entity.User;
import com.kakaopay.contactmanage.domain.user.service.UserService;
import com.kakaopay.contactmanage.help.config.security.SecurityConfig;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(controllers = UserController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)
        })
@Slf4j
@WithMockUser
class UserControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;

    @Test
    void 회원가입_정상_테스트() throws Exception {
        String content = createSignUpJsonContent("두승현","doosh17@naver.com", "qwer1234!@#");

        BDDMockito.given(userService.signUp(BDDMockito.any())).willReturn(BDDMockito.any());

        mvc.perform(post("/users/signup")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(content)
                .with(SecurityMockMvcRequestPostProcessors.csrf())
        ).andExpect(status().isCreated())
        .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void 회원가입_오류_테스트_비밀번호_복잡성_미충족() throws Exception{
        String content = createSignUpJsonContent("두승현", "doosh17@naver.com", "qwer12");

        BDDMockito.given(userService.signUp(BDDMockito.any())).willReturn(new User.OutDto());

        mvc.perform(post("/users/signup")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(content)
        ).andExpect(status().is4xxClientError())
        .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void 회원가입_오류_테스트_이메일_규칙_미충족() throws Exception{
        String content = createSignUpJsonContent("두승현", "doosh17naver.com", "qwer1234!@");

        BDDMockito.given(userService.signUp(BDDMockito.any())).willReturn(new User.OutDto());

        mvc.perform(post("/users/signup")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(content)
        ).andExpect(status().is4xxClientError())
        .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void 회원가입_오류_테스트_성명_누락() throws Exception{
        String content = createSignUpJsonContent(null, "doosh17@naver.com", "qwer1234!@");

        BDDMockito.given(userService.signUp(BDDMockito.any())).willReturn(new User.OutDto());

        mvc.perform(post("/users/signup")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content)
                ).andExpect(status().is4xxClientError())
                .andDo(MockMvcResultHandlers.print());
    }

    private String createSignUpJsonContent(String userNm, String userEmailAddr, String password) throws JsonProcessingException {
        User.SignUpDto signUpDto = new User.SignUpDto();
        signUpDto.setUserNm(userNm);
        signUpDto.setUserEmailAddr(userEmailAddr);
        signUpDto.setPassword(password);

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(signUpDto);
    }
}