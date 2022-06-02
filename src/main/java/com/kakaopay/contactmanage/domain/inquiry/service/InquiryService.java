package com.kakaopay.contactmanage.domain.inquiry.service;

import com.kakaopay.contactmanage.domain.inquiry.entity.Inquiry;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface InquiryService {
    public Inquiry.OutDto save(Inquiry.ParamDto paramDto);
    public Inquiry.OutListDto findAll(String customerId, Pageable pageable);
    public Inquiry.OutListDto findAll(Pageable pageable);
    public Inquiry.OutDto findById(Long id);
    public Optional<Inquiry> findInquiryById(Long id);
    public Inquiry.OutDto update(Inquiry.ParamDto paramDto, Long id);
}
