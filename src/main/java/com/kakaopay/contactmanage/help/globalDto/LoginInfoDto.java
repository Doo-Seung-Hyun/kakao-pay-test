package com.kakaopay.contactmanage.help.globalDto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginInfoDto {
    private Long id;
    private String userEmailAddr;
    private String userNm;
}
