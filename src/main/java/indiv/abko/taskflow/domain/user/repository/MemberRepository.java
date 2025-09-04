package indiv.abko.taskflow.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import indiv.abko.taskflow.domain.user.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
	boolean existsByUsername(String username);

	boolean existsByEmail(String email);
}
