package shop.kokodo.memberservice.controller;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shop.kokodo.memberservice.dto.MemberDto;
import shop.kokodo.memberservice.service.MemberService;
import shop.kokodo.memberservice.vo.RequestMember;
import shop.kokodo.memberservice.vo.ResponseMember;

@RestController
@RequestMapping("/")
public class MemberController {
    private Environment env;
    private MemberService memberService;

    @Autowired
    public MemberController(Environment env, MemberService memberService) {
        this.env = env;
        this.memberService = memberService;
    }

    //JWT CHECK
    @GetMapping("/health_check")
    public String status() {
        return String.format("It's Working in User Service on PORT %s", env.getProperty("local.server.port"));
    }

    //회원가입
    @PostMapping("/signup")
    public ResponseEntity<ResponseMember> createUser(@RequestBody RequestMember member) {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        MemberDto memberDto = mapper.map(member, MemberDto.class);
        memberService.createMember(memberDto);

        ResponseMember responseUser = mapper.map(memberDto, ResponseMember.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseUser);
    }

    //마이페이지
    @GetMapping("/member/{memberId}")
    public ResponseEntity<ResponseMember> getUser(@PathVariable("memberId") String loginId) {
        MemberDto memberDto = memberService.getMemberByLoginId(loginId);

        ResponseMember returnValue = new ModelMapper().map(memberDto, ResponseMember.class);
        return ResponseEntity.status(HttpStatus.OK).body(returnValue);
    }
}
