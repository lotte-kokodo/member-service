package shop.kokodo.memberservice.feign.service.interfaces;

import org.springframework.web.bind.annotation.RequestHeader;
import shop.kokodo.memberservice.feign.response.FeignResponse;

public interface MemberFeignService {

    FeignResponse.MemberAddress getMemberAddress(Long memberId);

}
