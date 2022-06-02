package com.kakaopay.contactmanage.domain.user.repository;

import com.kakaopay.contactmanage.domain.user.entity.User;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User,Long> {
    boolean existsByUserEmailAddr(String userEmailAddr);
    Optional<User> findByUserEmailAddr(String userEmailAddr);
}
