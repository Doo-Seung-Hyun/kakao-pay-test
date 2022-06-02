package com.kakaopay.contactmanage.domain.inquiry.repository;

import com.kakaopay.contactmanage.domain.inquiry.entity.Inquiry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InquiryRepository extends JpaRepository<Inquiry, Long> {
    Page<Inquiry> findAll(Pageable pageable);
    Page<Inquiry> findByCustomerId(String customerId, Pageable pageable);
}
