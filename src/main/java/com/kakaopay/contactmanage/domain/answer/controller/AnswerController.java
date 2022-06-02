package com.kakaopay.contactmanage.domain.answer.controller;

import com.kakaopay.contactmanage.domain.answer.entity.Answer;
import com.kakaopay.contactmanage.domain.answer.service.AnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/api/answers")
public class AnswerController {
    private final AnswerService answerService;

    @Autowired
    public AnswerController(AnswerService answerService) {
        this.answerService = answerService;
    }

    /**
     * /api/answers/{answerId}
     * GET
     * 답변내용 단건 조회
     */
    @GetMapping(path = "/{answerId}")
    public ResponseEntity<Answer.OutDto> findAnswer(@PathVariable Long answerId){

        Answer.OutDto outDto = answerService.findById(answerId);
        return new ResponseEntity<>(outDto, HttpStatus.OK);
    }

    /**
     * /api/answers/{answerId}
     * PUT
     * 답변내용 단건 수정
     */
    @PutMapping(path = "/{answerId}", consumes = "application/json")
    public ResponseEntity<Answer.OutDto> updateAnswer(
            @RequestBody @Valid Answer.ParamDto paramDto
            , @PathVariable Long answerId){

        paramDto.setId(answerId);
        Answer.OutDto outDto = answerService.save(paramDto);
        return new ResponseEntity<>(outDto, HttpStatus.OK);
    }

    /**
     * /api/answers
     * POST
     * 답변접수 (목록, 다건)
     */
    @PostMapping(consumes = "application/json")
    public ResponseEntity<Answer.AcceptOutDto> acceptAnswers(
            @RequestBody @Valid List<Answer.ParamDto> paramDtoList){

        Answer.AcceptOutDto acceptResultDto = answerService.acceptAnswers(paramDtoList);
        return new ResponseEntity<>(acceptResultDto
                , acceptResultDto.getCompletedCount()>0 ?
                    HttpStatus.CREATED : HttpStatus.CONFLICT);
    }
}
