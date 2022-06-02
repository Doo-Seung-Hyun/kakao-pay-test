package com.kakaopay.contactmanage.domain.answer.service;

import com.kakaopay.contactmanage.domain.answer.entity.Answer;
import com.kakaopay.contactmanage.domain.answer.repository.AnswerRepository;
import com.kakaopay.contactmanage.domain.inquiry.entity.Inquiry;
import com.kakaopay.contactmanage.domain.inquiry.service.InquiryService;
import com.kakaopay.contactmanage.domain.user.entity.User;
import com.kakaopay.contactmanage.domain.user.service.UserService;
import com.kakaopay.contactmanage.help.exception.ArgumentException;
import com.kakaopay.contactmanage.help.exception.NoDataFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class AnswerServiceImpl implements AnswerService{
    private final AnswerRepository answerRepository;
    private final InquiryService inquiryService;
    private final UserService userService;

    @Autowired
    public AnswerServiceImpl(AnswerRepository answerRepository, InquiryService inquiryService, UserService userService) {
        this.answerRepository = answerRepository;
        this.inquiryService = inquiryService;
        this.userService = userService;
    }

    @Override
    @Transactional
    public Answer.OutDto save(Answer.ParamDto paramDto) {
        User user = userService.findUserById(paramDto.getUserId())
                .orElseThrow(()->new ArgumentException("userId를 찾을 수 없습니다."));
        Inquiry inquiry = inquiryService.findInquiryById(paramDto.getInquiryId())
                .orElseThrow(()->new ArgumentException("inquiryId를 찾을 수 없습니다."));

        // 접수대상건인데 이미 접수가 된 경우 exception
        if("Y".equals(paramDto.getIsAccepting())){
            answerRepository.findByInquiryEquals(inquiry).ifPresent(
                    answer -> {throw new IllegalArgumentException();}
            );
        }

        Answer answer = Answer.builder()
                .id(paramDto.getId())
                .user(user)
                .inquiry(inquiry)
                .title(paramDto.getTitle())
                .content(paramDto.getContent())
                .isFinished(paramDto.getIsFinished())
                .acceptedDateTime(LocalDateTime.now())
                .finishedDateTime("Y".equals(paramDto.getIsFinished())?LocalDateTime.now() : null)
                .build();

        answerRepository.save(answer);

        return answer.getOutDto();
    }

    @Override
    public Answer.AcceptOutDto acceptAnswers(List<Answer.ParamDto> paramDtoList) {
        List<Answer.OutDto> outDtoList;

        int requestCount = paramDtoList.size();
        int completedCount = 0;
        int failedCount = 0;
        ArrayList<Answer.OutDto> completedList = new ArrayList<>();
        ArrayList<Answer.ParamDto> failedList = new ArrayList<>();

        for(Answer.ParamDto paramDto : paramDtoList){
            Answer.OutDto outDto;
            paramDto.setIsAccepting("Y"); //접수대상건으로 강제 할당
            paramDto.setIsFinished("N"); //접수대상은 완료여부(isFinished)가 N
            try {
                outDto = save(paramDto);
                completedList.add(outDto);
                completedCount++;
            } catch (IllegalArgumentException e) {
                log.debug("접수처리 중 충돌발생");
                log.debug("exception message ::: "+e.getMessage());
                log.debug(paramDto.toString());
                failedList.add(paramDto);
                failedCount++;
            }
        }

        return new Answer.AcceptOutDto(
                requestCount
                , completedCount
                , failedCount
                , completedList
                , failedList
        );
    }

    @Override
    public Answer.OutDto findById(Long id) {
        return answerRepository.findById(id).orElseThrow(
                ()->new NoDataFoundException()
        ).getOutDto();
    }
}
