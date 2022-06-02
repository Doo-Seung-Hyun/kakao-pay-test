package com.kakaopay.contactmanage.domain.inquiry.entity;

import com.kakaopay.contactmanage.domain.answer.entity.Answer;
import com.kakaopay.contactmanage.domain.user.entity.User;
import com.kakaopay.contactmanage.help.globalDto.LoginInfoDto;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Entity
@Table(name = "inquiry")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@BatchSize(size = 100)
public class Inquiry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="customer_id")
    private String customerId;

    private String title;
    private String content;

    @Column(name = "created_datetime")
    private LocalDateTime createdDateTime;

    @OneToMany(mappedBy = "inquiry", fetch = FetchType.LAZY)
    private List<Answer> answers;

    @Transient
    private DateTimeFormatter dateTimePattern = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss");

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

    public OutDto getOutDto(){
        OutDto outDto = new OutDto();
        outDto.setId(id);
        outDto.setTitle(title);
        outDto.setContent(content);
        outDto.setCustomerId(customerId);
        outDto.setCreatedDateTime(createdDateTime.format(dateTimePattern));
        outDto.setAnswered(false);
        if(answers!=null && answers.size()>0) {
            Answer answer = answers.get(0);
            outDto.setAnswered("Y".equals(answer.getIsFinished()));
            outDto.setAnswerId(answers.get(0).getId());
            outDto.setAcceptedDateTime(answer.getAcceptedDateTime().format(dateTimePattern));
            if(answer.getFinishedDateTime()!=null)
                outDto.setFinishedDateTime(answer.getFinishedDateTime().format(dateTimePattern));
        }
        if (SecurityContextHolder.getContext().getAuthentication().isAuthenticated()){
            User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            outDto.loginInfo = new LoginInfoDto(user.getId(), user.getUserEmailAddr(), user.getUserNm());
        }
        return outDto;
    }

    @Getter
    @Setter
    public static class ParamDto {
        private Long id;
        @NotBlank(message = "[customerId]는 필수입니다.")
        private String customerId;
        @NotBlank(message = "[title]는 필수입니다.")
        private String title;
        @NotBlank(message = "[content]는 필수입니다.")
        private String content;
        private String createdDateTime;
    }

    @Getter
    @Setter
    public static class OutDto {
        private Long id;
        private String customerId;
        private String title;
        private String content;
        private String createdDateTime;
        private boolean answered;
        private Long answerId;
        private String userNm;
        private String acceptedDateTime;
        private String finishedDateTime;
        private LoginInfoDto loginInfo;
    }

    @Getter
    public static class OutListDto {
        private int currPageNo;
        private int totPageNo;
        private int pageSize;
        public OutListDto(int currPageNo, int totPageNo, int pageSize){
            this.currPageNo=currPageNo;
            this.totPageNo=totPageNo;
            this.pageSize=pageSize;
            if (SecurityContextHolder.getContext().getAuthentication().isAuthenticated()){
                User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                this.loginInfo = new LoginInfoDto(user.getId(), user.getUserEmailAddr(), user.getUserNm());
            }
        }
        @Setter
        private List<OutDto> outList;
        private LoginInfoDto loginInfo;
    }
}
