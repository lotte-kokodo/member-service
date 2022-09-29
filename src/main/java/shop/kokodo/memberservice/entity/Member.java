package shop.kokodo.memberservice.entity;

import lombok.*;
import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "member")
public class Member extends BaseTimeEntity{

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String loginId;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String birthday;

    private String profileImageUrl;
    @Column(nullable = false)
    private String phoneNumber;
    @Column(nullable = false)
    private String address;
    private String grade;

    @Column(nullable = false, unique = true)
    private String encryptedPwd;
}
