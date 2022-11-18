package shop.kokodo.memberservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import shop.kokodo.memberservice.dto.MypageReviewDto;
import shop.kokodo.memberservice.dto.PageMypageReviewDto;

import java.util.List;

@FeignClient(name="product-service")
public interface MemberReviewClient {

    @GetMapping("/product-service/review/member/{memberId}/{currentpage}")
    PageMypageReviewDto findByMemberId(@PathVariable("memberId") long memberId ,@PathVariable("currentpage") int page);
}
