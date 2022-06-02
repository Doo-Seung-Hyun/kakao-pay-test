package com.kakaopay.contactmanage.domain.user.controller;

import com.kakaopay.contactmanage.domain.user.entity.User;
import com.kakaopay.contactmanage.domain.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping(path = "/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(path = "/signup")
    public ResponseEntity<User.OutDto> signUp(@RequestBody @Valid User.SignUpDto signUpDto){
        return new ResponseEntity<User.OutDto>(
                userService.signUp(signUpDto)
                , HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<User.OutListDto> findAll(){
        return new ResponseEntity<User.OutListDto>(
                userService.findAll(), HttpStatus.CREATED);
    }
}
