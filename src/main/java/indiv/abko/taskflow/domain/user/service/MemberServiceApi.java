package indiv.abko.taskflow.domain.user.service;

import java.util.Optional;

import indiv.abko.taskflow.domain.user.entity.Member;
import indiv.abko.taskflow.domain.user.entity.UserRole;

public interface MemberServiceApi {

	Member getByIdOrThrow(long memberId);

	Member createMember(String username, String password, String email, String name);

	boolean existsByUsername(String username);

	boolean existsByEmail(String email);

	// TODO: 혜준님을 위한 선물
	default Optional<Member> findByUsername(String username) {
		// username으로 멤버를 조회하여 Optional로 반환한다.
		Member member = Member.of("test", "$2a$10$rUtkFqIRH/oxz60GPSiSrOdVMAzcYHEykuzRC0XYHfvP38hONQyxm",
			"test@test.com", "홍길동", UserRole.USER);
		return Optional.ofNullable(member);
	}
}
