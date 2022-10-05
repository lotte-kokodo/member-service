package shop.kokodo.memberservice.vo.Response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
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

    @Builder
    public ResponseMember(String loginId, String name, String email, String birthday, String profileImageUrl, String phoneNumber, String address, String grade) {
        this.loginId = loginId;
        this.name = name;
        this.email = email;
        this.birthday = birthday;
        this.profileImageUrl = profileImageUrl;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.grade = grade;
    }
}
