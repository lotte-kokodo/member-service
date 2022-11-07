package shop.kokodo.memberservice.feign.service.interfaces;

import shop.kokodo.memberservice.feign.response.FeignResponse;

public interface MemberFeignService {

    // [주문 등록] 사용자 주소 조회
    FeignResponse.MemberDeliveryInfo getMemberDeliveryInfo(Long memberId);
}
