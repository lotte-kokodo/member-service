package shop.kokodo.memberservice.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import shop.kokodo.memberservice.dto.MemberDto;
import shop.kokodo.memberservice.entity.Member;

public interface MemberService extends UserDetailsService {
    MemberDto createMember(MemberDto userDto);
    MemberDto getMemberByLoginId(String userId);
    MemberDto getMemberDetailsByEmail(String userName);
}
