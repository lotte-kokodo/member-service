package shop.kokodo.memberservice.service;

import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import shop.kokodo.memberservice.dto.MemberDto;
import shop.kokodo.memberservice.dto.MemberResponse;
import shop.kokodo.memberservice.dto.MemberResponse.MemberDeliveryInfo;
import shop.kokodo.memberservice.dto.MemberResponse.MemberOfOrderSheet;
import shop.kokodo.memberservice.entity.Member;
import shop.kokodo.memberservice.repository.MemberRepository;
import shop.kokodo.memberservice.security.JwtTokenCreator;
import shop.kokodo.memberservice.vo.Request.RequestLogin;

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

    public void authenticate(RequestLogin requestLogin) {

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
    public MemberResponse.MemberDeliveryInfo getMemberDeliveryInfo(Long memberId) {
        return memberRepository.findById(memberId, MemberDeliveryInfo.class);
    }

    @Override
    public MemberResponse.MemberOfOrderSheet getMemberOrderInfo(Long memberId) {
        return memberRepository.findById(memberId, MemberOfOrderSheet.class);
    }

    @Override
    public Optional<Member> getMember(Long memberId) {
        return memberRepository.findById(memberId);
    }
}
