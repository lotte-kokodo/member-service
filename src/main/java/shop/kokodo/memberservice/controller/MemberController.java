package shop.kokodo.memberservice.controller;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import shop.kokodo.memberservice.dto.MemberDto;
import shop.kokodo.memberservice.dto.MemberResponse;
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
            return Response.failure(-1000,"회원가입에 실패했습니다.");
        } else {
            return Response.success();
        }
    }

    // 아이디 중복 확인
    @GetMapping("/signup/{loginId}")
    public Response checkId(@PathVariable("loginId") String loginId) {
        MemberDto memberDto = memberService.getMemberByLoginId(loginId);

        if (memberDto.getId() == null || memberDto.getLoginId().equals("")) {
            return Response.success("아이디 중복이 아닙니다.");
        } else {
            return Response.success("아이디 중복입니다.");
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
    public ResponseEntity getProductDetailReview(@PathVariable("memberId") long id) {
        MemberDto memberDto = memberService.getMemberById(id);
        RequestReview requestReview = new RequestReview(memberDto.getLoginId(), memberDto.getProfileImageUrl());

        return ResponseEntity.status(HttpStatus.OK).body(requestReview);
    }

    // [ 장바구니 ] 배송지 정보 (사용자 주소) 요청 API
    @GetMapping("/deliveryInfo")
    public Response getMemberAddress(@RequestHeader Long memberId) {
        MemberResponse.MemberDeliveryInfo data = memberService.getMemberDeliveryInfo(memberId);
        return Response.success(data);
    }

    // [ 주문서 ] 주문 고객 정보 요청 API
    @GetMapping("/orderInfo")
    public Response getMemberOrderInfo(@RequestHeader Long memberId) {
        MemberResponse.MemberOfOrderSheet data = memberService.getMemberOrderInfo(memberId);
        return Response.success(data);
    }

    // user id로 유효성 확인용 API
    @GetMapping("/id")
    public ResponseEntity getMemberById(@RequestParam(value = "memberId") Long memberId){
        boolean flag = memberService.getMember(memberId).isEmpty()? false : true;

        return ResponseEntity.status(HttpStatus.OK).body(flag);
    }
}
