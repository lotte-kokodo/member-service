package shop.kokodo.memberservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NaverTokenResponse {

    private String access_token;
    private String refresh_token;
    private String token_type;
    private Integer expires_in;
    private String error;
    private String error_description;

}
