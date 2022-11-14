package shop.kokodo.memberservice.service;

import org.springframework.web.bind.annotation.RequestHeader;
import shop.kokodo.memberservice.dto.CartMemberDto;
import shop.kokodo.memberservice.dto.MemberDto;
import shop.kokodo.memberservice.dto.OrderSheetMemberDto;
import shop.kokodo.memberservice.entity.Member;

import java.util.Optional;
import shop.kokodo.memberservice.vo.Request.RequestUpdateMember;

public interface MemberService {
    MemberDto createMember(MemberDto userDto);
    void updateMember(RequestUpdateMember dto);
    MemberDto getMemberByLoginId(String userId);
    MemberDto getMemberById(long id);

    // [주문 등록] 사용자 주소 조회
    CartMemberDto getCartMember(Long id);
    // [주문서 조회] 사용자 정보 조회
    OrderSheetMemberDto getOrderSheetMember(Long id);

    Optional<Member> getMember(Long memberId);
}
