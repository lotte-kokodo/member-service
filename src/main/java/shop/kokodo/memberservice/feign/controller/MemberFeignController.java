package shop.kokodo.memberservice.feign.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.kokodo.memberservice.feign.response.FeignResponse;
import shop.kokodo.memberservice.feign.service.interfaces.MemberFeignService;

@RestController
@RequestMapping("/members/feign")
public class MemberFeignController {

    private final MemberFeignService memberFeignService;

    @Autowired
    public MemberFeignController(
        MemberFeignService memberFeignService) {
        this.memberFeignService = memberFeignService;
    }

    @GetMapping("/address")
    public FeignResponse.MemberAddress getMemberAddress(@RequestHeader Long memberId) {
        return memberFeignService.getMemberAddress(memberId);
    }

}
