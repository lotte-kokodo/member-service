package shop.kokodo.memberservice.dto;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class NaverMemberInfoRequest {

    @Value("${oauth.naver.me-url}")
    private String meUrl;


}
