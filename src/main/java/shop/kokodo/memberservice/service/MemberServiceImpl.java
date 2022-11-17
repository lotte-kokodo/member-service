package shop.kokodo.memberservice.service;

import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import shop.kokodo.memberservice.dto.CartMemberDto;
import shop.kokodo.memberservice.dto.MemberDto;
import shop.kokodo.memberservice.dto.OrderSheetMemberDto;
import shop.kokodo.memberservice.entity.Member;
import shop.kokodo.memberservice.repository.MemberRepository;
import shop.kokodo.memberservice.security.JwtTokenCreator;
import shop.kokodo.memberservice.vo.Request.RequestUpdateMember;

@Service
@Slf4j
public class MemberServiceImpl implements MemberService {

    MemberRepository memberRepository;
    BCryptPasswordEncoder passwordEncoder;

    JwtTokenCreator jwtTokenCreator;

    @Autowired
    public MemberServiceImpl(MemberRepository memberRepository, BCryptPasswordEncoder passwordEncoder,
        JwtTokenCreator jwtTokenCreator) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenCreator = jwtTokenCreator;
    }

    @Transactional
    @Override
    public MemberDto createMember(@RequestBody MemberDto memberDto) {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        Member member = mapper.map(memberDto, Member.class);

        member.setEncryptedPwd(passwordEncoder.encode(memberDto.getPassword()));
        memberRepository.save(member);
        MemberDto returnMemberDto = mapper.map(member,MemberDto.class);

        return returnMemberDto;
    }
    @Transactional(readOnly = true)
    @Override
    public void updateMember(@RequestBody RequestUpdateMember req) {
        Member member = memberRepository.findByLoginId(req.getLoginId());
        if (member == null) {
            throw new IllegalArgumentException("등록되지 않은 사용자입니다.");
        }
        member.update(req);
        memberRepository.save(member);
    }

    @Override
    public MemberDto getMemberByLoginId(String loginId) {
        Member member = memberRepository.findByLoginId(loginId);
        if(member == null) return new MemberDto();

        MemberDto memberDto = new ModelMapper().map(member,MemberDto.class);
        return memberDto;
    }

    @Override
    public MemberDto getMemberById(long id) {
        Member member = memberRepository.findById(id).orElse(new Member());

        if(member == null){
            throw new UsernameNotFoundException("Member not found");
        }

        MemberDto memberDto = new ModelMapper().map(member,MemberDto.class);
        return memberDto;
    }

    @Override
    public CartMemberDto getCartMember(Long id) {
        return memberRepository.findById(id, CartMemberDto.class);
    }

    @Override
    public OrderSheetMemberDto getOrderSheetMember(Long id) {
        return memberRepository.findById(id, OrderSheetMemberDto.class);
    }

    @Override
    public Optional<Member> getMember(Long memberId) {
        return memberRepository.findById(memberId);
    }

    @Override
    public Boolean checkMemberInfo(Long memberId) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> {
                throw new IllegalArgumentException("등록되지 않은 회원입니다.");
            });
        return (member.getAddress() != null && member.getPhoneNumber() != null);
    }
}
