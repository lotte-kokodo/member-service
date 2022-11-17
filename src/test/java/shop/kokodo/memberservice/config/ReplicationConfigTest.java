package shop.kokodo.memberservice.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import shop.kokodo.memberservice.dto.MemberDto;
import shop.kokodo.memberservice.service.MemberService;

//@SpringBootTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ReplicationConfigTest {

//    @Autowired
//    MemberService memberService;
//
//    MemberDto memberDto;
//
//    final String loginId = "loginId";
//
//    @BeforeEach
//    public void setUp(){
//        memberDto= MemberDto.builder()
//                .loginId(loginId)
//                .name("name0")
//                .email("email@email.com")
//                .password("password")
//                .birthday("2022-01-01")
//                .profileImageUrl("image")
//                .phoneNumber("010-0000-0000")
//                .address("address")
//                .grade("grade")
//                .build();
//    }
//
//    @Test
//    @DisplayName("member DB wrtie ")
//    public void member_DB_wrte(){
//        memberService.createMember(memberDto);
//    }
//
//    @Test
//    @DisplayName("member DB read ")
//    public void member_DB_read(){
//        memberService.createMember(memberDto);
//
//        memberService.getMemberByLoginId(loginId);
//        memberService.getMemberByLoginId(loginId);
//        memberService.getMemberByLoginId(loginId);
//    }
}
