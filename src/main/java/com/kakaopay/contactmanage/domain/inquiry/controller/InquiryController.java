package com.kakaopay.contactmanage.domain.inquiry.controller;

import com.kakaopay.contactmanage.domain.inquiry.entity.Inquiry;
import com.kakaopay.contactmanage.domain.inquiry.service.InquiryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/inquiries")
public class InquiryController {
    private final InquiryService inquiryService;

    @Autowired
    public InquiryController(InquiryService inquiryService) {
        this.inquiryService = inquiryService;
    }


    /**
     * url : /api/inquiries/{inquiryId}
     * GET
     * 질문내용 단건 조회
     */
    @GetMapping(path = "/{inquiryId}")
    public ResponseEntity<Inquiry.OutDto> findInquiry(@PathVariable Long inquiryId){

        Inquiry.OutDto outDto = inquiryService.findById(inquiryId);
        return new ResponseEntity<>(outDto, HttpStatus.OK);
    }

    /**
     * url : /api/inquiries?customerId(생략가능)&size=10&page=0
     * GET
     * 질문내용 목록 다건 조회
     */
    @GetMapping
    public ResponseEntity<Inquiry.OutListDto> findInquiries(
            @RequestParam(required = false) String customerId
            ,@PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable){

        Inquiry.OutListDto outListDto;
        if(customerId!=null)
            outListDto = inquiryService.findAll(customerId, pageable);
        else
            outListDto = inquiryService.findAll(pageable);

        return new ResponseEntity<>(outListDto, HttpStatus.OK);
    }

    /**
     * url : /api/inquiries
     * POST
     * 질문내용 단건 입력
     */
    @PostMapping(consumes = "application/json")
    public ResponseEntity<Inquiry.OutDto> insertInquiry(@RequestBody Inquiry.ParamDto paramDto){

        Inquiry.OutDto outDto = inquiryService.save(paramDto);
        return new ResponseEntity<>(outDto, HttpStatus.CREATED);
    }

    /**
     * url : /api/inquiries/{inquiryId}
     * PUT
     * 질문내용 단건 수정
     */
    @PutMapping(path = "/{inquiryId}", consumes = "application/json")
    public ResponseEntity<Inquiry.OutDto> updateInquiry(@RequestBody Inquiry.ParamDto paramDto, @PathVariable Long inquiryId){
        Inquiry.OutDto outDto = inquiryService.save(paramDto);
        return new ResponseEntity<>(outDto, HttpStatus.OK);
    }


}
