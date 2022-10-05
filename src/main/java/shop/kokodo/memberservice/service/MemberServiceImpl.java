package shop.kokodo.memberservice.service;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import shop.kokodo.memberservice.dto.MemberDto;
import shop.kokodo.memberservice.entity.Member;
import shop.kokodo.memberservice.repository.MemberRepository;

import java.util.ArrayList;

@Service
@Slf4j
public class MemberServiceImpl implements MemberService{

    MemberRepository memberRepository;
    BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public MemberServiceImpl(MemberRepository memberRepository, BCryptPasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        Member member = memberRepository.findByLoginId(loginId);

        if(member == null){
            throw new UsernameNotFoundException("Member not found");
        }

        return new User(member.getLoginId(), member.getEncryptedPwd(),
                true,true,true,true,
                new ArrayList<>());
    }

    @Override
    public MemberDto createMember(MemberDto memberDto) {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        Member member = mapper.map(memberDto, Member.class);

        member.setEncryptedPwd(passwordEncoder.encode(memberDto.getPassword()));
        memberRepository.save(member);
        MemberDto returnMemberDto = mapper.map(member,MemberDto.class);

        return returnMemberDto;
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
}
