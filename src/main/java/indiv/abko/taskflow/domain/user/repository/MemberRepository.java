package indiv.abko.taskflow.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import indiv.abko.taskflow.domain.user.entity.Member;
import indiv.abko.taskflow.domain.user.exception.MemberErrorCode;
import indiv.abko.taskflow.global.exception.BusinessException;

public interface MemberRepository extends JpaRepository<Member, Long> {
	default Member findByIdOrElseThrow(Long id) {
		return findById(id)
			.orElseThrow(() -> new BusinessException(MemberErrorCode.MEMBER_NOT_FOUND));
	}
}
