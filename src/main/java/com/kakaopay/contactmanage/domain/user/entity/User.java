package com.kakaopay.contactmanage.domain.user.entity;

import com.kakaopay.contactmanage.domain.inquiry.entity.Inquiry;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Entity
@Table(name="user")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="user_email_addr")
    private String userEmailAddr;

    @Column
    private String password;

    @Column(name="user_nm")
    private String userNm;

    @Column(name="created_datetime")
    private LocalDateTime createdDateTime;

    @Transient
    private String location;

    @Transient
    @Setter
    private Collection<? extends GrantedAuthority> authorities;

    @Override
    public int hashCode() {
        return id.intValue();
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj)
            return true;
        if(obj == null || this.getClass() != obj.getClass())
            return false;

        return this.id.equals(((User)obj).getId());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        return authorities;
    }

    @Override
    public String getUsername() {
        return userEmailAddr;
    }

    // 계정만료 없음
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // 계정잠김 없음
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // 비밀번호 만료 없음
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Getter
    @Setter
    public static class SignUpDto{
        @NotBlank
        @Email(message = "이메일 형식이 올바르지 않습니다.")
        private String userEmailAddr;

        @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\w).{8,20}",
                message = "비밀번호는 영어대소문자, 숫자, 특수문자 3가지를 조합하여 8~20자리로 입력해야 합니다.")
        @Pattern(regexp = "^\\S+$",
                message = "비밀번호는 공백을 넣을 수 없습니다.")
        private String password;

        @NotBlank(message = "이름이 올바르지 않습니다.")
        private String userNm;
    }

    @Getter
    @Setter
    public static class SignInDto{
        @NotBlank
        @Email
        private String userEmailAddr;

        @NotBlank
        private String password;
    }

    @Getter
    @Setter
    public static class OutDto{
        private Long id;
        private String userEmailAddr;
        private String userNm;
        private String createdDateTime;
        private String location;

        public void setCreatedDateTime(LocalDateTime createdDateTime) {
            this.createdDateTime = createdDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        }
    }

    @Getter
    @AllArgsConstructor
    public static class OutListDto{
        private List<OutDto> users;
    }

    public OutDto getOutDto(){
        OutDto outDto = new OutDto();
        outDto.setId(id);
        outDto.setUserEmailAddr(userEmailAddr);
        outDto.setUserNm(userNm);
        outDto.setCreatedDateTime(createdDateTime);
        outDto.setLocation(id!=null? "/users/"+id: "");

        return outDto;
    }
}
