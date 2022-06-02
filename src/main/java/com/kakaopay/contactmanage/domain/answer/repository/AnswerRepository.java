package com.kakaopay.contactmanage.domain.answer.repository;

import com.kakaopay.contactmanage.domain.answer.entity.Answer;
import com.kakaopay.contactmanage.domain.inquiry.entity.Inquiry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AnswerRepository extends JpaRepository<Answer,Long> {
    public Optional<Answer> findByInquiryEquals(Inquiry inquiry);
}
