package com.kakaopay.contactmanage.domain.inquiry.repository;

import com.kakaopay.contactmanage.domain.answer.entity.Answer;
import com.kakaopay.contactmanage.domain.inquiry.entity.Inquiry;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class InquiryRepositoryTest {
    @Autowired
    private InquiryRepository inquiryRepository;

    private static List<Inquiry.ParamDto> paramDtoList;
    private static Inquiry inquiry;

    @BeforeAll
    static void 초기설정(){
        paramDtoList = new ArrayList<>();
        loadTestParamList();
    }

    @Test
    @Order(1)
    void save(){
        for(Inquiry.ParamDto paramDto : paramDtoList){
            inquiry = Inquiry.builder()
                    .customerId(paramDto.getCustomerId())
                    .title(paramDto.getTitle())
                    .content(paramDto.getContent())
                    .createdDateTime(LocalDateTime.parse(
                            paramDto.getCreatedDateTime(),DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
                    ).build();
            inquiryRepository.save(inquiry);
        }
    }

    @Test
    @Order(2)
    void findByCustomerId() {
        save();
        Inquiry.ParamDto dto = paramDtoList.get(2);

        Page<Inquiry> page = inquiryRepository.findByCustomerId(dto.getCustomerId(),PageRequest.of(0,20));

        assertThat(page.getTotalElements()).isEqualTo(
                paramDtoList.stream().filter(v->
                        v.getCustomerId().equals(dto.getCustomerId())
                ).count());
    }

    @Test
    @Order(3)
    void findAll() {
        save();

        Page<Inquiry> page = inquiryRepository.findAll(PageRequest.of(0,20));

        assertThat(page.getTotalElements()).isGreaterThan(paramDtoList.size());
        
        // N+1 테스트 (BatchSize 동작테스트)
        // Inquiry - Answer가 참조관계이므로 N+1 문제 발생하는지 테스트
        List<Answer> answers = page.get().filter(v->v.getAnswers()!=null).findFirst().get().getAnswers();
        System.out.println(answers.get(0).getContent());
    }

    private static void loadTestParamList() {
        paramDtoList.add(new Inquiry.ParamDto());
        Inquiry.ParamDto paramDto = paramDtoList.get(paramDtoList.size()-1);
        paramDto.setCustomerId("doosh17");
        paramDto.setTitle("문의드려요");
        paramDto.setContent("내일 몇시까지 영업하나요?");
        paramDto.setCreatedDateTime("20220527112324");

        paramDtoList.add(new Inquiry.ParamDto());
        paramDto = paramDtoList.get(paramDtoList.size()-1);
        paramDto.setCustomerId("ddeetest1@");
        paramDto.setTitle("배송이 안와요");
        paramDto.setContent("빨리보내주세요 ㅠㅠ 애가탑니다");
        paramDto.setCreatedDateTime("20220528140505");

        paramDtoList.add(new Inquiry.ParamDto());
        paramDto = paramDtoList.get(paramDtoList.size()-1);
        paramDto.setCustomerId("qweqwe1");
        paramDto.setTitle("배송좀 보내주세요");
        paramDto.setContent("언제까지 배송될까요? 현기증나요");
        paramDto.setCreatedDateTime("20220529183333");

        paramDtoList.add(new Inquiry.ParamDto());
        paramDto = paramDtoList.get(paramDtoList.size()-1);
        paramDto.setCustomerId("qweqwe1");
        paramDto.setTitle("아직멀었어요?");
        paramDto.setContent("대답도 없으시고.........빨리 배송해주세요");
        paramDto.setCreatedDateTime("20220529183333");

        paramDtoList.add(new Inquiry.ParamDto());
        paramDto = paramDtoList.get(paramDtoList.size()-1);
        paramDto.setCustomerId("victory321");
        paramDto.setTitle("요금제를 변경하고 싶어요");
        paramDto.setContent("지금 쓰는 요금제가 너무 과다한 것 같아요 새로운 요금제 없나요?");
        paramDto.setCreatedDateTime("20220529214314");
    }
}