package com.kakaopay.contactmanage.help.exception;

public class NoDataFoundException extends RuntimeException {
    public NoDataFoundException(){
        this("결과를 찾을 수 없습니다.");
    }
    public NoDataFoundException(String s) {
        super(s);
    }
}
