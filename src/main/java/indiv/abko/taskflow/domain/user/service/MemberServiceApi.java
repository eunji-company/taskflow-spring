package indiv.abko.taskflow.domain.user.service;

import indiv.abko.taskflow.domain.user.entity.Member;

import java.util.Optional;

public interface MemberServiceApi {

	Member getByIdOrThrow(long memberId);

	Member createMember(String username, String password, String email, String name);

	boolean existsByUsername(String username);

	boolean existsByEmail(String email);

	default boolean exstsById(long memberId) {
		//id가 존재한다면 true, 없다면 false 구현해주세요 혜준님
		return false;
	}

	Optional<Member> findByUsername(String username);
}
