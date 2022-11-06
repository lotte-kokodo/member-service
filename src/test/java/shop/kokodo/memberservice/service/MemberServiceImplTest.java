package shop.kokodo.memberservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import shop.kokodo.memberservice.dto.MemberDto;
import shop.kokodo.memberservice.entity.Member;
import shop.kokodo.memberservice.repository.MemberRepository;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceImplTest {

    @InjectMocks
    MemberServiceImpl memberServiceImpl;

    @Mock
    MemberRepository memberRepository;

    @Spy
    BCryptPasswordEncoder encoder;

    MemberDto memberDto;
    Member member;

    @BeforeEach
    void setUp() {
        member = Member.builder()
                .id(1L)
                .loginId("worhs95")
                .name("OhJaegon")
                .email("opve555@gmail.com")
                .password("test1234")
                .birthday("950428")
                .profileImageUrl("https://avatars.githubusercontent.com/u/114142626?s=200&v=4")
                .phoneNumber("01012341234")
                .address("서울 성동구 성수2가1동")
                .grade("1")
                .build();

        memberDto = MemberDto.builder()
                .loginId("worhs95")
                .name("OhJaegon")
                .email("opve555@gmail.com")
                .password("test1234")
                .birthday("950428")
                .profileImageUrl("https://avatars.githubusercontent.com/u/114142626?s=200&v=4")
                .phoneNumber("01012341234")
                .address("서울 성동구 성수2가1동")
                .grade("1")
                .build();
    }

    @Test
    @DisplayName("멤버 생성 성공")
    void createMember() {
        //given
        encoder = new BCryptPasswordEncoder();
        memberDto.setEncryptedPwd(encoder.encode(member.getPassword()));

        doReturn(member).when(memberRepository).save(any(Member.class));

        //when
        MemberDto returnMemberDto = memberServiceImpl.createMember(memberDto);

        //then (평문 비밀번호가 서로 같은데 암호화 된 비밀번호가 다른지 확인)
        assertThat(encoder.matches(returnMemberDto.getPassword(),member.getPassword())).isFalse();

        // verify
        verify(memberRepository, times(1)).save(any(Member.class));
    }

    @Test
    @DisplayName("로그인 아이디로 멤버 조회 성공")
    void getMemberByLoginId() {
        // given
        memberRepository.save(member);
        doReturn(member).when(memberRepository)
                .findByLoginId(member.getLoginId());

        // when
        final MemberDto memberDto1 = memberServiceImpl.getMemberByLoginId(member.getLoginId());

        // then
        assertThat(memberDto1.getName()).isEqualTo(member.getName());
    }

    @Test
    @DisplayName("멤버 아이디로 멤버 조회 성공")
    void getMemberById() {
        // given
        doReturn(Optional.of(member)).when(memberRepository)
                .findById(1L);

        // when
        final MemberDto memberDto1 = memberServiceImpl.getMemberById(member.getId());

        // then
        assertThat(memberDto1.getName()).isEqualTo(member.getName());
    }
}