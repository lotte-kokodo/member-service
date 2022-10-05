package shop.kokodo.memberservice.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import shop.kokodo.memberservice.entity.Member;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    Member member;

    @BeforeEach
    public void setUp() {
        member = Member.builder()
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
    @DisplayName("로그인 아이디로 Member 조회")
    void findByLoginId() {
        //given
        memberRepository.save(member);

        //when
        Member test = memberRepository.findByLoginId(member.getLoginId());

        //then
        Assertions.assertEquals(member.getId(), test.getId());
        Assertions.assertEquals(member.getName(), test.getName());
    }
}