package shop.kokodo.memberservice.feign.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shop.kokodo.memberservice.feign.repository.MemberFeignRepository;
import shop.kokodo.memberservice.feign.response.FeignResponse.MemberDeliveryInfo;
import shop.kokodo.memberservice.feign.service.interfaces.MemberFeignService;

@Service
public class MemberFeignServiceImpl implements MemberFeignService {

    private final MemberFeignRepository memberFeignRepository;

    @Autowired
    public MemberFeignServiceImpl(
        MemberFeignRepository memberFeignRepository) {
        this.memberFeignRepository = memberFeignRepository;
    }

    @Override
    public MemberDeliveryInfo getMemberDeliveryInfo(Long memberId) {
        return memberFeignRepository.findById(memberId, MemberDeliveryInfo.class);
    }
}
