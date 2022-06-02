package com.kakaopay.contactmanage.domain.answer.service;

import com.kakaopay.contactmanage.domain.answer.entity.Answer;

import java.util.List;

public interface AnswerService {
    public Answer.OutDto save(Answer.ParamDto paramDto);
    public Answer.AcceptOutDto acceptAnswers(List<Answer.ParamDto> paramDtoList);
    public Answer.OutDto findById(Long id);
}
