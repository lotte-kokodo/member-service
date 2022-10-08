package shop.kokodo.memberservice.feign.repository;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import shop.kokodo.memberservice.entity.Member;

@Repository
public interface MemberFeignRepository extends CrudRepository<Member, Long> {
    <T> T findById(Long id, Class<T> type);

    <T> List<T> findByIdIn(List<Long> ids, Class<T> type);
}
