package shop.kokodo.memberservice.dto;

import lombok.*;

import java.util.Date;


@Data
@NoArgsConstructor
@ToString
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

    @Builder
    public MemberDto(Long id, String loginId, String name, String email, String password, String birthday, String profileImageUrl, String phoneNumber, String address, String grade, Date createdAt, String encryptedPwd) {
        this.id = id;
        this.loginId = loginId;
        this.name = name;
        this.email = email;
        this.password = password;
        this.birthday = birthday;
        this.profileImageUrl = profileImageUrl;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.grade = grade;
        this.createdAt = createdAt;
        this.encryptedPwd = encryptedPwd;
    }
}
