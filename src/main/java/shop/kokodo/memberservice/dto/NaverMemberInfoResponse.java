package shop.kokodo.memberservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class NaverMemberInfoResponse {

    private String resultcode;
    private String message;

    private Response response;

    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private String id;
        private String name;
        private String email;
        private String birthyear;
        private String birthday;
        private String profile_image;
        private String mobile;
    }
}
