package com.kakaopay.contactmanage.domain.inquiry.service;

import com.kakaopay.contactmanage.domain.inquiry.entity.Inquiry;
import com.kakaopay.contactmanage.domain.inquiry.repository.InquiryRepository;
import static org.assertj.core.api.Assertions.*;

import com.kakaopay.contactmanage.help.exception.NoDataFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class InquiryServiceImplTest {
    @Mock
    private InquiryRepository inquiryRepository;

    InquiryService inquiryService;
    private String customerId;
    private String title;
    private String content;
    private Long id;

    @BeforeEach
    void 초기설정(){
        inquiryService = new InquiryServiceImpl(inquiryRepository);
        customerId = "doosh17";
        title = "예약문의드립니다";
        content = "안녕하세요 혹시 내일 점심 3명 예약 가능할까요?\n오전 11:30분까지 갈게요";
        id = 10l;
    }


    @Test
    void save(){
        Inquiry.ParamDto paramDto = new Inquiry.ParamDto();
        paramDto.setCustomerId(customerId);
        paramDto.setTitle(title);
        paramDto.setContent(content);

        Inquiry result = Inquiry.builder()
                        .id(id)
                        .customerId(customerId)
                        .title(title)
                        .content(content)
                        .createdDateTime(LocalDateTime.now())
                        .build();

        BDDMockito.given(inquiryRepository.save(BDDMockito.any()))
                .willReturn(result);

        Inquiry.OutDto outDto = inquiryService.save(paramDto);

        assertThat(outDto.getId()).isEqualTo(id);
    }

    @Test
    void findById_데이터_없는_경우(){
        Long noDataId = 999l;
        BDDMockito.given(inquiryRepository.findById(noDataId))
                .willReturn(Optional.empty());

        try {
            inquiryService.findById(noDataId);
        } catch (Exception e) {
            assertThat(e.getCause() instanceof NoDataFoundException);
        }
    }

    @Test
    void findById(){
        Long noDataId = 999l;
        Inquiry result = Inquiry.builder()
                .id(id)
                .customerId(customerId)
                .title(title)
                .content(content)
                .createdDateTime(LocalDateTime.now())
                .build();

        BDDMockito.given(inquiryRepository.findById(id))
                .willReturn(Optional.of(result));

        Inquiry.OutDto outDto = inquiryService.findById(id);

        assertThat(outDto.getTitle()).isEqualTo(title);
        assertThat(outDto.getCustomerId()).isEqualTo(customerId);
        assertThat(outDto.getId()).isEqualTo(id);
    }

}