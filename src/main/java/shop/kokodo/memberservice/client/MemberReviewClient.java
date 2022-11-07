package shop.kokodo.memberservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import shop.kokodo.memberservice.dto.MypageReviewDto;

import java.util.List;

@FeignClient(name="product-service", url = "http://localhost:8001")
public interface MemberReviewClient {

    @GetMapping("/product-service/review/member/{memberId}")
    List<MypageReviewDto> findByMemberId(@PathVariable("memberId") long memberId);
}
