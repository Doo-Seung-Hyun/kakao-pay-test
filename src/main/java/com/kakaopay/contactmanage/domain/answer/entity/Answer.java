package com.kakaopay.contactmanage.domain.answer.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakaopay.contactmanage.domain.inquiry.entity.Inquiry;
import com.kakaopay.contactmanage.domain.user.entity.User;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "answer")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "inquiry_id", referencedColumnName = "id")
    private Inquiry inquiry;

    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    private String title;
    private String content;

    @Column(name="is_finished")
    private String isFinished;

    @Column(name = "accepted_datetime")
    private LocalDateTime acceptedDateTime;

    @Column(name = "finished_datetime")
    private LocalDateTime finishedDateTime;

    @Override
    public int hashCode() {
        return id.intValue();
    }

    @Transient
    private DateTimeFormatter dateTimePattern = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss");

    @Override
    public boolean equals(Object obj) {
        if(this == obj)
            return true;
        if(obj == null || this.getClass() != obj.getClass())
            return false;

        return this.id.equals(((User)obj).getId());
    }

    @Getter
    @Setter
    @ToString
    public static class ParamDto{
        private Long id;
        @NotNull(message = "[userId]를 입력해주세요.")
        private Long userId;
        @NotNull(message = "[inquiryId]를 입력해주세요.")
        private Long inquiryId;
        private String title;
        private String content;
        @Pattern(regexp = "^[YN]$",
                message = "[isFinished]는 Y 또는 N 이어야 합니다.")
        private String isFinished;
        private String isAccepting;
        private String createdDateTime;
        private String finishedDateTime;
    }

    @Getter
    @Setter
    public static class OutDto{
        private Long id;
        private Map<Object,Object> user;
        private Map<Object,Object> inquiry;
        private String title;
        private String content;
        private String isFinished;
        private String createdDateTime;
        private String finishedDateTime;
    }

    @Getter
    public static class AcceptParamDto{
        private List<ParamDto> acceptList;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AcceptOutDto {
        private int requestCount;
        private int completedCount;
        private int failedCount;

        private List<OutDto> completedList;
        private List<ParamDto> failedList;
    }

    public OutDto getOutDto(){
        OutDto outDto = new OutDto();
        ObjectMapper objectMapper = new ObjectMapper();

        outDto.setId(id);
        outDto.setTitle(title);
        outDto.setContent(content);
        outDto.setIsFinished(isFinished);
        outDto.setCreatedDateTime(acceptedDateTime.format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss")));
        if(finishedDateTime!=null)
            outDto.setFinishedDateTime(finishedDateTime.format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss")));

        if(user!=null)
            outDto.setUser(objectMapper.convertValue(user.getOutDto(),Map.class));
        if(inquiry!=null)
            outDto.setInquiry(objectMapper.convertValue(inquiry.getOutDto(),Map.class));

        return outDto;
    }
}

