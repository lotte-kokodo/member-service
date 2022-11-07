package shop.kokodo.memberservice.service;

import shop.kokodo.memberservice.dto.NaverCallbackParam;
import shop.kokodo.memberservice.dto.NaverMemberInfoResponse;

public interface OauthService {

    NaverMemberInfoResponse getMemberInfoFromNaver(NaverCallbackParam param);

}
