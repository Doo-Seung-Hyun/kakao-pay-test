package com.kakaopay.contactmanage.domain.user.service;

import com.kakaopay.contactmanage.domain.user.entity.User;
import com.kakaopay.contactmanage.domain.user.repository.UserRepository;
import com.kakaopay.contactmanage.help.exception.DuplicatedEmailAddrException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final HttpServletRequest httpServletRequest;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository
            , HttpServletRequest httpServletRequest
            , PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.httpServletRequest=httpServletRequest;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User.OutDto signUp(User.SignUpDto signUpDto) {
        if(userRepository.existsByUserEmailAddr(signUpDto.getUserEmailAddr())){
            throw new DuplicatedEmailAddrException();
        }
        LocalDateTime currentDateTime = LocalDateTime.now();

        User user = User.builder()
                    .userEmailAddr(signUpDto.getUserEmailAddr())
                    .userNm(signUpDto.getUserNm())
                    .password(encryptPassword(signUpDto.getPassword()))
                    .createdDateTime(currentDateTime)
                    .build();
        userRepository.save(user);

        return user.getOutDto();
    }

    @Override
    public User.OutListDto findAll() {
        User.OutListDto userList = new User.OutListDto(
                userRepository.findAll().stream().map(User::getOutDto).collect(Collectors.toList())
        );
        return userList;
    }

    @Override
    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }


    public String encryptPassword(String plainPassword) {
        return passwordEncoder.encode(plainPassword);
    }
}
