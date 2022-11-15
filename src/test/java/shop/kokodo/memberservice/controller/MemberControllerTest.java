package shop.kokodo.memberservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import shop.kokodo.memberservice.dto.MemberDto;
import shop.kokodo.memberservice.entity.Member;
import shop.kokodo.memberservice.repository.MemberRepository;
import shop.kokodo.memberservice.vo.Request.RequestMember;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.RequestEntity.post;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Transactional
class MemberControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    Member member;
    Member member1;
    MemberDto memberDto;
    RequestMember requestMember;

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

        member1 = Member.builder()
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

        requestMember = RequestMember.builder()
                .loginId("worhs95")
                .name("OhJaegon")
                .email("opve555@gmail.com")
                .password("test1234")
                .birthday("1995-04-28")
                .profileImageUrl("https://avatars.githubusercontent.com/u/114142626?s=200&v=4")
                .phoneNumber("010-1234-1234")
                .address("서울 성동구 성수2가1동")
                .grade("1")
                .build();
    }

    @Test
    @DisplayName("회원가입 생성 성공")
    void createUser() throws Exception {
        this.mockMvc.perform(RestDocumentationRequestBuilders.post("/member/signup")
                        .content(objectMapper.writeValueAsString(requestMember))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("member-rest-controller/member-signup",
                        responseFields(
                                fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공여부"),
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("상태코드")
                        ))
                );
    }

    @Test
    @DisplayName("회원가입 시 아이디 중복 확인")
    void checkId() throws Exception {
        memberRepository.save(member);

        this.mockMvc.perform(get("/member/signup/{loginId}",member.getLoginId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("member-rest-controller/member-checkId",
                                pathParameters(
                                        parameterWithName("loginId").description("회원가입 시 아이디 중복 확인")
                                ),
                                responseFields(
                                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공여부"),
                                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("상태코드"),
                                        fieldWithPath("result.msg").type(JsonFieldType.STRING).description("결과")
                                )
                        )
                );
    }

    @Test
    @DisplayName("마이페이지 회원 정보 조회 성공")
    void getUser() throws Exception {
        memberRepository.save(member1);

        this.mockMvc.perform(get("/member/myPage/{memberId}", member1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("member-rest-controller/member-getUser",
                                pathParameters(
                                        parameterWithName("memberId").description("PK로 멤버 조회")
                                ),
                                responseFields(
                                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공여부"),
                                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("상태코드"),
                                        fieldWithPath("result.data.loginId").type(JsonFieldType.STRING).description("멤버 로그인 아이디"),
                                        fieldWithPath("result.data.name").type(JsonFieldType.STRING).description("멤버 이름"),
                                        fieldWithPath("result.data.email").type(JsonFieldType.STRING).description("멤버 이메일"),
                                        fieldWithPath("result.data.birthday").type(JsonFieldType.STRING).description("멤버 생일"),
                                        fieldWithPath("result.data.profileImageUrl").type(JsonFieldType.STRING).description("멤버 프로필 이미지"),
                                        fieldWithPath("result.data.phoneNumber").type(JsonFieldType.STRING).description("멤버 전화번호"),
                                        fieldWithPath("result.data.address").type(JsonFieldType.STRING).description("멤버 주소"),
                                        fieldWithPath("result.data.grade").type(JsonFieldType.STRING).description("멤버 등급")
                                )
                        )
                );
    }

    @Test
    @DisplayName("상품상세 리뷰를 위한 회원 아이디 및 프로필 주소 조회 성공")
    void getProductDetailReview() throws Exception{
        memberRepository.save(member);

        this.mockMvc.perform(get("/member/productDetail/{memberId}",member.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("member-rest-controller/member-productDetail",
                                pathParameters(
                                        parameterWithName("memberId").description("리뷰 위한 회원 정보 조회")
                                ),
                                responseFields(
                                        fieldWithPath("loginId").type(JsonFieldType.STRING).description("아이디"),
                                        fieldWithPath("profileImageUrl").type(JsonFieldType.STRING).description("프로필이미지url")
                                )
                        )
                );

    }
}