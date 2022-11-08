package shop.kokodo.memberservice.service;

import shop.kokodo.memberservice.dto.NaverCallbackParam;
import shop.kokodo.memberservice.dto.NaverMemberInfoResponse;
import shop.kokodo.memberservice.dto.response.Response;

public interface OauthService {

    NaverMemberInfoResponse getMemberInfoFromNaver(NaverCallbackParam param);

    Response authenticateWithNaver(NaverCallbackParam param);

}
