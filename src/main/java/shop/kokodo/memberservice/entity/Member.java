package shop.kokodo.memberservice.entity;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.stereotype.Service;

import javax.persistence.*;


@Getter
@Setter
@Entity
@NoArgsConstructor
@ToString
@Table(name = "member")
public class Member extends BaseTimeEntity {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;
    @Column(nullable = false, unique = true)
    private String loginId;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String birthday;
    private String profileImageUrl;
    @Column(nullable = false)
    private String phoneNumber;
    @Column()
    private String address;
    @Column(nullable = false)
    private String grade;

    @Builder
    public Member(Long id, String loginId, String name, String email, String password, String birthday, String profileImageUrl, String phoneNumber, String address, String grade) {
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
    }
}
