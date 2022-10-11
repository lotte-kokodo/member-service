package shop.kokodo.memberservice.feign.service.interfaces;

import org.springframework.web.bind.annotation.RequestHeader;
import shop.kokodo.memberservice.feign.response.FeignResponse;

public interface MemberFeignService {

    // [주문 등록] 사용자 주소 조회
    FeignResponse.MemberAddress getMemberAddress(Long memberId);

    // [주문서 조회] 사용자 정보 조회
    FeignResponse.MemberOfOrderSheet getMemberOrderInfo(@RequestHeader Long memberId);
}
