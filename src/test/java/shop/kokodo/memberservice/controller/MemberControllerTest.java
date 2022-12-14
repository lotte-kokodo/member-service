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
                .address("?????? ????????? ??????2???1???")
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
                .address("?????? ????????? ??????2???1???")
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
                .address("?????? ????????? ??????2???1???")
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
                .address("?????? ????????? ??????2???1???")
                .grade("1")
                .build();
    }

    @Test
    @DisplayName("???????????? ?????? ??????")
    void createUser() throws Exception {
        this.mockMvc.perform(RestDocumentationRequestBuilders.post("/member/signup")
                        .content(objectMapper.writeValueAsString(requestMember))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("member-rest-controller/member-signup",
                        responseFields(
                                fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("????????????"),
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("????????????")
                        ))
                );
    }

    @Test
    @DisplayName("???????????? ??? ????????? ?????? ??????")
    void checkId() throws Exception {
        memberRepository.save(member);

        this.mockMvc.perform(get("/member/signup/{loginId}",member.getLoginId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("member-rest-controller/member-checkId",
                                pathParameters(
                                        parameterWithName("loginId").description("???????????? ??? ????????? ?????? ??????")
                                ),
                                responseFields(
                                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("????????????"),
                                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("????????????"),
                                        fieldWithPath("result.msg").type(JsonFieldType.STRING).description("??????")
                                )
                        )
                );
    }

    @Test
    @DisplayName("??????????????? ?????? ?????? ?????? ??????")
    void getUser() throws Exception {
        memberRepository.save(member1);

        this.mockMvc.perform(get("/member/myPage/{memberId}", member1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("member-rest-controller/member-getUser",
                                pathParameters(
                                        parameterWithName("memberId").description("PK??? ?????? ??????")
                                ),
                                responseFields(
                                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("????????????"),
                                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("????????????"),
                                        fieldWithPath("result.data.loginId").type(JsonFieldType.STRING).description("?????? ????????? ?????????"),
                                        fieldWithPath("result.data.name").type(JsonFieldType.STRING).description("?????? ??????"),
                                        fieldWithPath("result.data.email").type(JsonFieldType.STRING).description("?????? ?????????"),
                                        fieldWithPath("result.data.birthday").type(JsonFieldType.STRING).description("?????? ??????"),
                                        fieldWithPath("result.data.profileImageUrl").type(JsonFieldType.STRING).description("?????? ????????? ?????????"),
                                        fieldWithPath("result.data.phoneNumber").type(JsonFieldType.STRING).description("?????? ????????????"),
                                        fieldWithPath("result.data.address").type(JsonFieldType.STRING).description("?????? ??????"),
                                        fieldWithPath("result.data.grade").type(JsonFieldType.STRING).description("?????? ??????")
                                )
                        )
                );
    }

    @Test
    @DisplayName("???????????? ????????? ?????? ?????? ????????? ??? ????????? ?????? ?????? ??????")
    void getProductDetailReview() throws Exception{
        memberRepository.save(member);

        this.mockMvc.perform(get("/member/productDetail/{memberId}",member.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("member-rest-controller/member-productDetail",
                                pathParameters(
                                        parameterWithName("memberId").description("?????? ?????? ?????? ?????? ??????")
                                ),
                                responseFields(
                                        fieldWithPath("loginId").type(JsonFieldType.STRING).description("?????????"),
                                        fieldWithPath("profileImageUrl").type(JsonFieldType.STRING).description("??????????????????url")
                                )
                        )
                );

    }
}