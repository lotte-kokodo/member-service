package shop.kokodo.memberservice.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseMember {
    private String loginId;
    private String name;
    private String email;
    private String birthday;
    private String profileImageUrl;
    private String phoneNumber;
    private String address;
    private String grade;
}
