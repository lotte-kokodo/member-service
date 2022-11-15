package shop.kokodo.memberservice.feign.service.interfaces;

import shop.kokodo.memberservice.feign.response.OrderMemberDto;

public interface MemberFeignService {

    // [주문 등록] 사용자 주소 조회
    OrderMemberDto getOrderMember(Long memberId);
}
