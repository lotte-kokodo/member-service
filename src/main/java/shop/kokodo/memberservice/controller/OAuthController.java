package shop.kokodo.memberservice.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.kokodo.memberservice.dto.oauth.OAuthCallbackParam;
import shop.kokodo.memberservice.dto.response.Response;
import shop.kokodo.memberservice.service.OauthService;
import shop.kokodo.memberservice.vo.Response.ResponseLogin;

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
    public Response authenticateWithNaver(OAuthCallbackParam param) {
        ResponseLogin resp = oauthService.authenticateWithNaver(param);
        return Response.success(resp);
    }

    @GetMapping("/kakao")
    public Response authenticateWithKakao(OAuthCallbackParam param) {
        ResponseLogin resp = oauthService.authenticateWithKakao(param);
        return Response.success(resp);
    }

}
