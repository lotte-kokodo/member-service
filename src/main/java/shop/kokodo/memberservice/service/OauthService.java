package shop.kokodo.memberservice.service;

import shop.kokodo.memberservice.dto.oauth.OAuthCallbackParam;
import shop.kokodo.memberservice.vo.Response.ResponseLogin;

public interface OauthService {

    ResponseLogin authenticateWithNaver(OAuthCallbackParam param);
    ResponseLogin authenticateWithKakao(OAuthCallbackParam param);
}
