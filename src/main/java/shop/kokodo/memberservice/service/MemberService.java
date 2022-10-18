package shop.kokodo.memberservice.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.RequestHeader;
import shop.kokodo.memberservice.dto.MemberDto;
import shop.kokodo.memberservice.dto.MemberResponse;

public interface MemberService extends UserDetailsService {
    MemberDto createMember(MemberDto userDto);
    MemberDto getMemberByLoginId(String userId);
    MemberDto getMemberById(long id);

    // [주문 등록] 사용자 주소 조회
    MemberResponse.MemberDeliveryInfo getMemberDeliveryInfo(Long memberId);
    // [주문서 조회] 사용자 정보 조회
    MemberResponse.MemberOfOrderSheet getMemberOrderInfo(@RequestHeader Long memberId);
}
