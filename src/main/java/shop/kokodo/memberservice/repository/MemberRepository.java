package shop.kokodo.memberservice.repository;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import shop.kokodo.memberservice.entity.Member;

public interface MemberRepository extends CrudRepository<Member, Long> {
    Member findByLoginId(String loginId);

    <T> T findById(Long id, Class<T> type);
    <T> List<T> findByIdIn(List<Long> ids, Class<T> type);
}
