package shop.kokodo.memberservice.entity;

import lombok.*;
import org.springframework.stereotype.Service;

import javax.persistence.*;


@Getter
@Setter
@Entity
@NoArgsConstructor
@ToString
@Table(name = "member")
public class Member extends BaseTimeEntity{

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;
    @Column(nullable = false, unique = true)
    private String loginId;
    private String name;
    private String email;
    private String password;
    private String birthday;
    private String profileImageUrl;
    private String phoneNumber;
    private String address;
    private String grade;
    private String encryptedPwd;

    @Builder
    public Member(Long id, String loginId, String name, String email, String password, String birthday, String profileImageUrl, String phoneNumber, String address, String grade, String encryptedPwd) {
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
        this.encryptedPwd = encryptedPwd;
    }
}
