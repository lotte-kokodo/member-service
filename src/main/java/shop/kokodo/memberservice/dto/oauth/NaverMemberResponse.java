package shop.kokodo.memberservice.dto.oauth;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class NaverMemberResponse {

    private String id;
    private String name;
    private String email;
    private String birthyear;
    private String birthday;
    private String profile_image;
    private String mobile;

}
