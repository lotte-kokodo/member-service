package shop.kokodo.memberservice.controller;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.kokodo.memberservice.dto.MemberDto;
import shop.kokodo.memberservice.dto.response.Response;
import shop.kokodo.memberservice.service.MemberService;
import shop.kokodo.memberservice.vo.Request.RequestMember;
import shop.kokodo.memberservice.vo.Request.RequestReview;
import shop.kokodo.memberservice.vo.Response.ResponseMember;
import shop.kokodo.memberservice.vo.Response.ResponseOrderMemberInfo;

@RestController
@RequestMapping("/member")
public class MemberController {
    private Environment env;
    private MemberService memberService;

    @Autowired
    public MemberController(Environment env, MemberService memberService) {
        this.env = env;
        this.memberService = memberService;
    }

    //회원가입
    @PostMapping("/signup")
    public Response createUser(@RequestBody RequestMember member) {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        MemberDto memberDto = mapper.map(member, MemberDto.class);

        memberService.createMember(memberDto);

        return Response.success();
    }

    // 아이디 중복 확인
    @GetMapping("/signup/{loginId}")
    public Response checkId(@PathVariable("loginId") String loginId) {
        MemberDto memberDto = memberService.getMemberByLoginId(loginId);

        if (memberDto.getId() == null || memberDto.getLoginId().equals("")) {
            return Response.success("success");
        } else {
            return Response.success("overlap");
        }
    }

    // 마이페이지
    @GetMapping("/myPage/{memberId}")
    public Response getUser(@PathVariable("memberId") long id) {
        MemberDto memberDto = memberService.getMemberById(id);
        ResponseMember returnValue = new ModelMapper().map(memberDto, ResponseMember.class);

        return Response.success(returnValue);
    }

    // 상품디테일 리뷰 작성자 요청 API
    @GetMapping("/productDetail/{memberId}")
    public Response getProductDetailReview(@PathVariable("memberId") long id) {
        MemberDto memberDto = memberService.getMemberById(id);
        RequestReview requestReview = new RequestReview(memberDto.getLoginId(), memberDto.getProfileImageUrl());

        return Response.success(requestReview);
    }

    // 주문서 사용자 정보 요청 API
    @GetMapping("/{memberId}/orderInfo")
    public Response getMemberOrderInfo(@PathVariable("memberId") Long id) {
        MemberDto memberDto = memberService.getOrderMemberInfo(id);
        ResponseOrderMemberInfo responseOrderMemberInfo = ResponseOrderMemberInfo.builder()
            .name(memberDto.getName())
            .email(memberDto.getEmail())
            .phoneNumber(memberDto.getPhoneNumber())
            .address(memberDto.getAddress())
            .build();

        return Response.success(responseOrderMemberInfo);
    }
}
