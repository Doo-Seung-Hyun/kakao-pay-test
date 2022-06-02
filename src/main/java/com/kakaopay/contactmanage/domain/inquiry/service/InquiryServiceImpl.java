package com.kakaopay.contactmanage.domain.inquiry.service;

import com.kakaopay.contactmanage.domain.inquiry.entity.Inquiry;
import com.kakaopay.contactmanage.domain.inquiry.repository.InquiryRepository;
import com.kakaopay.contactmanage.help.exception.NoDataFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InquiryServiceImpl implements InquiryService{
    private final InquiryRepository inquiryRepository;

    @Autowired
    public InquiryServiceImpl(InquiryRepository inquiryRepository) {
        this.inquiryRepository = inquiryRepository;
    }

    @Override
    public Inquiry.OutDto save(Inquiry.ParamDto paramDto) {
        Inquiry inquiry = Inquiry.builder()
                        .id(paramDto.getId())
                        .customerId(paramDto.getCustomerId())
                        .title(paramDto.getTitle())
                        .content(paramDto.getContent())
                        .createdDateTime(LocalDateTime.now())
                        .build();

        inquiry = inquiryRepository.save(inquiry);

        return inquiry.getOutDto();
    }

    @Override
    public Inquiry.OutListDto findAll(Pageable pageable) {
        Page<Inquiry> page = inquiryRepository.findAll(pageable);

        return getOutListDto(page);
    }

    @Override
    public Inquiry.OutListDto findAll(String customerId, Pageable pageable) {
        Page<Inquiry> page = inquiryRepository.findByCustomerId(customerId,pageable);

        return getOutListDto(page);
    }

    private Inquiry.OutListDto getOutListDto(Page<Inquiry> page) {
        Inquiry.OutListDto outListDto = new Inquiry.OutListDto(
                page.getNumber()+1
                , page.getTotalPages()
                , page.getSize()
        );

        outListDto.setOutList(page.get().map(Inquiry::getOutDto).collect(Collectors.toList()));

        return outListDto;
    }

    @Override
    public Inquiry.OutDto findById(Long id) {
        Inquiry inquiry = inquiryRepository.findById(id).orElseThrow(()
                -> {
            throw new NoDataFoundException("요청하신 내용을 찾을 수 없습니다.");
        });

        return inquiry.getOutDto();
    }

    @Override
    public Optional<Inquiry> findInquiryById(Long id) {
        return inquiryRepository.findById(id);
    }

    @Override
    public Inquiry.OutDto update(Inquiry.ParamDto paramDto, Long id) {
        findById(id);
        paramDto.setId(id);
        return save(paramDto);
    }

}
