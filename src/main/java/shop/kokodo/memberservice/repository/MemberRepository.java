package shop.kokodo.memberservice.repository;

import org.springframework.data.repository.CrudRepository;
import shop.kokodo.memberservice.entity.Member;

public interface MemberRepository extends CrudRepository<Member, Long> {
    Member findByLoginId(String loginId);
}
