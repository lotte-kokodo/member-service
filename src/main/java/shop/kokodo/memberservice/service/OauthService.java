package shop.kokodo.memberservice.service;

import shop.kokodo.memberservice.dto.NaverCallbackParam;
import shop.kokodo.memberservice.vo.Response.ResponseLogin;

public interface OauthService {

    ResponseLogin authenticateWithNaver(NaverCallbackParam param);

}
