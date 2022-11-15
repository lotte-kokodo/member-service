package shop.kokodo.memberservice.controller;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
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

import shop.kokodo.memberservice.client.MemberReviewClient;
import shop.kokodo.memberservice.dto.CartMemberDto;

import shop.kokodo.memberservice.dto.MemberDto;
import shop.kokodo.memberservice.dto.OrderSheetMemberDto;
import shop.kokodo.memberservice.dto.PageMypageReviewDto;
import shop.kokodo.memberservice.dto.response.Response;
import shop.kokodo.memberservice.service.MemberService;
import shop.kokodo.memberservice.vo.Request.RequestMember;
import shop.kokodo.memberservice.vo.Request.RequestReview;
import shop.kokodo.memberservice.vo.Request.RequestUpdateMember;
import shop.kokodo.memberservice.vo.Response.ResponseMember;

import java.util.ArrayList;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/member")
public class MemberController {
    private Environment env;
    private MemberService memberService;
    private MemberReviewClient memberReviewClient;
    private CircuitBreakerFactory circuitBreakerFactory;

    @Autowired
    public MemberController(Environment env, MemberService memberService, MemberReviewClient memberReviewClient, CircuitBreakerFactory circuitBreakerFactory) {
        this.env = env;
        this.memberService = memberService;
        this.memberReviewClient = memberReviewClient;
        this.circuitBreakerFactory = circuitBreakerFactory;
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
            return Response.failure(-1001,"아이디 중복입니다.");
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
    public Response getUser(@RequestBody RequestUpdateMember req) {
        memberService.updateMember(req);
        return Response.success("success");
    }

    // 상품상세 리뷰를 위한 회원 아이디 및 프로필 주소 조회
    @GetMapping("/productDetail/{memberId}")
    public ResponseEntity getProductDetailReview(@PathVariable("memberId") long id) {
        MemberDto memberDto = memberService.getMemberById(id);
        RequestReview requestReview = new RequestReview(memberDto.getLoginId(), memberDto.getProfileImageUrl());

        return ResponseEntity.status(HttpStatus.OK).body(requestReview);
    }

    // [Feign Client] myPage 에서 내가 작성한 상품 리뷰 조회 API
    @GetMapping("/mypage/review/{memberId}/{currentpage}")
    public Response findByMemberId(@PathVariable long memberId, @PathVariable("currentpage") int page){
        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("reviewCircuit");
        PageMypageReviewDto list = circuitBreaker.run(() -> memberReviewClient.findByMemberId(memberId, page),
                throwable -> new PageMypageReviewDto(new ArrayList<>(),0));

        return Response.success(list);
    }


    // [ 장바구니 ] 배송지 정보 (사용자 주소) 요청 API
    @GetMapping("/cart")
    public Response getMemberAddress(@RequestHeader Long memberId) {
        CartMemberDto data = memberService.getCartMember(memberId);
        return Response.success(data);
    }

    // [ 주문서 ] 주문 고객 정보 요청 API
    @GetMapping("/order")
    public Response getMemberOrderInfo(@RequestHeader Long memberId) {
        OrderSheetMemberDto data = memberService.getOrderSheetMember(memberId);
        return Response.success(data);
    }

    // user id로 유효성 확인용 API
    @GetMapping("/id")
    public ResponseEntity getMemberById(@RequestParam(value = "memberId") Long memberId){
        boolean flag = memberService.getMember(memberId).isEmpty()? false : true;

        return ResponseEntity.status(HttpStatus.OK).body(flag);
    }

    // [주문] 주문 시 사용자 정보 적용됐는지 확인
    @GetMapping("/check/info")
    public ResponseEntity<Boolean> checkMemberInfoApply(@RequestHeader Long memberId) {
        Boolean isMemberInfoRegistered = memberService.checkMemberInfo(memberId);
        return ResponseEntity.ok(isMemberInfoRegistered);
    }
}
