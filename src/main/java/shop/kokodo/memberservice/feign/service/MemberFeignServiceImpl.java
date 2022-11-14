package shop.kokodo.memberservice.feign.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shop.kokodo.memberservice.feign.repository.MemberFeignRepository;
import shop.kokodo.memberservice.feign.response.OrderMemberDto;
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
    public OrderMemberDto getOrderMember(Long memberId) {
        return memberFeignRepository.findById(memberId, OrderMemberDto.class);
    }
}
