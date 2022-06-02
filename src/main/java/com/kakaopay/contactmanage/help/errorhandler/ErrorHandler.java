package com.kakaopay.contactmanage.help.errorhandler;

import com.kakaopay.contactmanage.domain.user.entity.User;
import com.kakaopay.contactmanage.help.exception.ArgumentException;
import com.kakaopay.contactmanage.help.exception.DuplicatedEmailAddrException;
import com.kakaopay.contactmanage.help.exception.LoginFailureException;
import com.kakaopay.contactmanage.help.exception.NoDataFoundException;
import com.kakaopay.contactmanage.help.globalDto.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(DuplicatedEmailAddrException.class)
    public ResponseEntity<ErrorResponseDto> duplicatedEmailAddrHandle(@RequestBody User.SignUpDto signUpDto, HttpServletRequest request){
        ErrorResponseDto<User.SignUpDto> dto =
                new ErrorResponseDto<>(request.getRequestURI(), signUpDto, "중복된 이메일 주소입니다.");
        return new ResponseEntity<ErrorResponseDto>(dto, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(LoginFailureException.class)
    public ResponseEntity<ErrorResponseDto> loginFailureHandle(@RequestBody User.SignInDto signInDto, HttpServletRequest request){
        ErrorResponseDto<User.SignInDto> dto =
                new ErrorResponseDto<>(request.getRequestURI(), signInDto, "로그인에 실패하였습니다. 아이디 또는 패스워드가 잘못됐습니다.");
        return new ResponseEntity<ErrorResponseDto>(dto, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map> paramNotValidHandle(HttpServletRequest request, MethodArgumentNotValidException ex) {
        ErrorResponseDto<Object> dto =
                new ErrorResponseDto<>(request.getRequestURI()
                        ,ex.getBindingResult().getTarget()
                        ,ex.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        return new ResponseEntity<Map>(dto.convert2Map(), getHttpStatus(ex));
    }

    @ExceptionHandler({NoDataFoundException.class, ArgumentException.class})
    public ResponseEntity<Map> commonErrorHandle(@RequestBody Map<String,Object> requestMap, HttpServletRequest request,Exception ex) {
        ErrorResponseDto<Object> dto =
                new ErrorResponseDto<>(request.getRequestURI()
                        ,requestMap
                        ,ex.getMessage());
        return new ResponseEntity<Map>(dto.convert2Map(), getHttpStatus(ex));
    }

    private HttpStatus getHttpStatus(Exception ex){
        HttpStatus httpStatus = HttpStatus.CONFLICT;

        if(ex instanceof NoDataFoundException){
            httpStatus = HttpStatus.NOT_FOUND;
        }else if(ex instanceof ArgumentException
                || ex instanceof MethodArgumentNotValidException){
            httpStatus = HttpStatus.BAD_REQUEST;
        }

        return httpStatus;
    }
}
