package shop.kokodo.memberservice.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import shop.kokodo.memberservice.dto.NaverCallbackParam;
import shop.kokodo.memberservice.dto.NaverMemberInfoResponse;
import shop.kokodo.memberservice.dto.NaverTokenRequest;
import shop.kokodo.memberservice.dto.response.Response;
import shop.kokodo.memberservice.service.OauthService;

@RestController
@RequestMapping("/oauth")
@Slf4j
public class OAuthController {

    private final OauthService oauthService;

    public OAuthController(
        OauthService oauthService) {
        this.oauthService = oauthService;
    }

    /**
     * @param param 네이버 로그인 정보 제공 동의 성공 여부 응답
     * @return
     */
    @GetMapping("/naver")
    public Response loginByNaver(NaverCallbackParam param) {

        NaverMemberInfoResponse resp = oauthService.getMemberInfoFromNaver(param);
        return Response.success(resp);
    }

}
