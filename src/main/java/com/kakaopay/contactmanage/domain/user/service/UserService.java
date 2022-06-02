package com.kakaopay.contactmanage.domain.user.service;

import com.kakaopay.contactmanage.domain.user.entity.User;

import java.util.Optional;

public interface UserService {
    public User.OutDto signUp(User.SignUpDto signUpDto);
    public User.OutListDto findAll();
    public Optional<User> findUserById(Long id);
}
