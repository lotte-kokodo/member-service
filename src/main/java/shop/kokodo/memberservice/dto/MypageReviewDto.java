package shop.kokodo.memberservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;

@Data
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MypageReviewDto {
    private long id;
    private String createdDate;
    private long productId;
    private String content;
    private double rating;
    private long memberId;
    private String displayName;
    private String thumbnail;
}
