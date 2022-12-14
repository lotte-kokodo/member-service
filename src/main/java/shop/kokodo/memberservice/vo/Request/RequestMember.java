package shop.kokodo.memberservice.vo.Request;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class RequestMember {

    private String loginId;

    @NotNull(message = "Name cannot be null")
    @Size(min = 2, message = "Name not be less than two characters")
    private String name;

    @NotNull(message = "Email cannot be null")
    @Size(min = 2, message = "Email not be less than two characters")
    @Email
    private String email;

    @NotNull(message = "Password cannot be null")
    @Size(min = 8, message = "Password not be less than eight characters")
    private String password;

    @NotNull(message = "birthday cannot be null")
    @Size(min = 6, message = "birthday not be less than six characters")
    private String birthday;

    private String profileImageUrl;

    @NotNull(message = "birthday cannot be null")
    @Size(min = 10, message = "birthday not be less than ten characters")
    private String phoneNumber;

    @NotNull(message = "address cannot be null")
    @Size(min = 6, message = "address not be less than six characters")
    private String address;

    @NotNull(message = "grade cannot be null")
    @Size(min = 2, message = "grade not be less than two characters")
    private String grade;

    @Builder
    public RequestMember(String loginId, String name, String email, String password, String birthday, String profileImageUrl, String phoneNumber, String address, String grade) {
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
