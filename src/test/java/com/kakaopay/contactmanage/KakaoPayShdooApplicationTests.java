package com.kakaopay.contactmanage;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakaopay.contactmanage.domain.answer.entity.Answer;
import com.kakaopay.contactmanage.domain.answer.service.AnswerService;
import com.kakaopay.contactmanage.domain.inquiry.entity.Inquiry;
import com.kakaopay.contactmanage.help.exception.NoDataFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@WithMockUser
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class KakaoPayShdooApplicationTests {

    private MockMvc mvc;

    @Autowired
    private WebApplicationContext ctx;

    private static ObjectMapper objectMapper = new ObjectMapper();

    private static Long inquiryId;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(ctx)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();
    }

    @Test
    @Order(1)
    void 질문_Inquiry_등록() throws Exception {
        String content = "{\"customerId\": \"doosh1919\", \"title\": \"테스트입니다\"," +
                " \"content\": \"테스트 본문입니다 테스트가 잘 됐으면 좋겠습니다.\"}";

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("/api/inquiries")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();

        Inquiry.OutDto outDto = convert2Map(mvcResult, Inquiry.OutDto.class);

        Assertions.assertThat("doosh1919").isEqualTo(outDto.getCustomerId());
        inquiryId = outDto.getId();
    }

    @Test
    @Order(2)
    void 질문_Inquiry_수정() throws Exception {
        String content = "{\"customerId\": \"doosh1919\", \"title\": \"제목을 바꿨습니다.\"," +
                " \"content\": \"테스트 본문입니다 테스트가 잘 됐으면 좋겠습니다.\"}";

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put("/api/inquiries/{inquiryId}",inquiryId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        Inquiry.OutDto outDto = convert2Map(mvcResult, Inquiry.OutDto.class);

        Assertions.assertThat("doosh1919").isEqualTo(outDto.getCustomerId());
        Assertions.assertThat(outDto.getTitle()).contains("제목을 바꿨습니다");
    }


    @Test
    @Order(3)
    void 질문_Inquiry_조회() throws Exception {

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/api/inquiries/{inquiryId}",inquiryId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        Inquiry.OutDto outDto = convert2Map(mvcResult, Inquiry.OutDto.class);

        Assertions.assertThat("doosh1919").isEqualTo(outDto.getCustomerId());
        Assertions.assertThat(outDto.getContent()).contains("테스트 본문입니다");
    }

    @Test
    @Order(4)
    void 질문_Inquiry_조회_잘못된ID() throws Exception {

        try {
            MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/api/inquiries/{inquiryId}",9999)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isNotFound())
                    .andReturn();
            fail();
        } catch (Exception e) {
            assertThat(e instanceof NoDataFoundException);
        }
    }

    @Test
    @Order(5)
    void 질문_Inquiry_대량조회() throws Exception {
        int size=10;
        int page=2;
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/api/inquiries")
                .param("size",Integer.toString(size))
                .param("page",Integer.toString(page-1))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        Inquiry.OutListDto outListDto = convert2Map(mvcResult, Inquiry.OutListDto.class);

        assertThat(page).isEqualTo(outListDto.getCurrPageNo());
        assertThat(size).isEqualTo(outListDto.getPageSize());

    }

    @Test
    @Order(11)
    void 답변_Answer_접수() throws Exception {
        String content = "[{\"userId\": 2, \"inquiryId\": 9, \"isAccepting\": \"Y\"}," +
                "{\"userId\": 2, \"inquiryId\": 10, \"isAccepting\": \"Y\"}]";

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("/api/answers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();

        Map<String,Object> resultMap = convert2Map(mvcResult,Map.class);

        Assertions.assertThat(2).isEqualTo(resultMap.get("requestCount"));
        Assertions.assertThat(2).isEqualTo(resultMap.get("completedCount"));
    }

    @Test
    @Order(12)
    void 답변_Answer_접수_중복() throws Exception {
        String content = "[{\"userId\": 4, \"inquiryId\": 9, \"isAccepting\": \"Y\"}]";

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("/api/answers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isConflict()) // 접수건이 모두 충돌일 경우 CONFLICT
                .andReturn();

        Answer.AcceptOutDto acceptOutDto = convert2Map(mvcResult,Answer.AcceptOutDto.class);

        Assertions.assertThat(1).isEqualTo(acceptOutDto.getFailedCount());
        Assertions.assertThat(0).isEqualTo(acceptOutDto.getCompletedCount());
    }

    @Test
    @Order(13)
    void 답변_Answer_저장() throws Exception {
        Long answerId = 9l;
        String content = "{\"userId\": 4, \"inquiryId\": 9, \"isFinished\": \"Y\", \"title\" : \"고객님 답변드립니다\", \"content\" : \"테스트 답변입니다 안녕하세요 안녕하십니까\"}";

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put("/api/answers/{answerId}",answerId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        Answer.OutDto outDto = convert2Map(mvcResult,Answer.OutDto.class);

        //Assertions.assertThat(outDto.getInquiry().get("id")).isEqualTo(9);
        Assertions.assertThat(outDto.getTitle()).contains("고객님 답변드립니다");
        Assertions.assertThat(outDto.getContent()).contains("테스트 답변입니다");
    }

    @Test
    @Order(14)
    void 답변_Answer_저장_userid_누락_validation() throws Exception {
        Long answerId = 9l;
        String content = "{\"inquiryId\": 9, \"isFinished\": \"Y\", \"title\" : \"고객님 답변드립니다\", \"content\" : \"테스트 답변입니다 안녕하세요 안녕하십니까\"}";

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put("/api/answers/{answerId}",answerId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();
    }



    @Test
    @Order(15)
    void 답변_Answer_조회() throws Exception {
        Long answerId = 9l;

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/api/answers/{answerId}",answerId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())
                .andReturn();

        Answer.OutDto outDto = convert2Map(mvcResult, Answer.OutDto.class);

        Assertions.assertThat(outDto.getInquiry().get("id")).isEqualTo(9);
        Assertions.assertThat(outDto.getTitle()).contains("고객님 답변드립니다");
        Assertions.assertThat(outDto.getContent()).contains("테스트 답변입니다");
    }

    private <T> T convert2Map(MvcResult mvcResult, Class<T> targetClass) throws UnsupportedEncodingException, JsonProcessingException {
        return objectMapper.readValue(mvcResult.getResponse().getContentAsString(), targetClass);
    }

}
