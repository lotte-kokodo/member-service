package shop.kokodo.memberservice.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;
import shop.kokodo.memberservice.dto.MemberDto;
import shop.kokodo.memberservice.dto.response.Response;
import shop.kokodo.memberservice.service.MemberService;
import shop.kokodo.memberservice.vo.Request.RequestMember;
import shop.kokodo.memberservice.vo.Request.RequestReview;
import shop.kokodo.memberservice.vo.Request.RequestUpdateMember;
import shop.kokodo.memberservice.vo.Response.ResponseMember;

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

        memberDto = memberService.createMember(memberDto);

        if (memberDto.getId() == null || memberDto.getLoginId().equals("")) {
            return Response.failure(401,"fail");
        } else {
            return Response.success("success");
        }
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

    // 마이페이지 수정
    @PostMapping("/myPage")
    public Response getUser(@RequestBody RequestUpdateMember member) {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        MemberDto memberDto = mapper.map(member, MemberDto.class);

        memberDto = memberService.createMember(memberDto);

        if (memberDto.getId() == null || memberDto.getLoginId().equals("")) {
            return Response.failure(401,"fail");
        } else {
            return Response.success("success");
        }
    }

    // 상품디테일 리뷰 작성자 요청 API
    @GetMapping("/productDetail/{memberId}")
    public Response getProductDetailReview(@PathVariable("memberId") long id) {
        MemberDto memberDto = memberService.getMemberById(id);
        RequestReview requestReview = new RequestReview(memberDto.getLoginId(), memberDto.getProfileImageUrl());

        return Response.success(requestReview);
    }
}
