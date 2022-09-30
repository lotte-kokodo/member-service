package shop.kokodo.memberservice.dto;

import lombok.Data;
import java.util.Date;


@Data
public class MemberDto {
    private Long id;
    private String loginId;
    private String name;
    private String email;
    private String password;

    private String birthday;
    private String profileImageUrl;
    private String phoneNumber;
    private String address;
    private String grade;
    private Date createdAt;

    private String encryptedPwd;
}
