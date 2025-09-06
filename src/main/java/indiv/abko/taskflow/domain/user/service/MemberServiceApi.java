package indiv.abko.taskflow.domain.user.service;

import java.util.Optional;

import indiv.abko.taskflow.domain.user.entity.Member;

public interface MemberServiceApi {

	Member getByIdOrThrow(long memberId);

	Member createMember(String username, String password, String email, String name);

	boolean existsByUsername(String username);

	boolean existsByEmail(String email);

	// TODO: 혜준님을 위한 선물
	Optional<Member> findByUsername(String username);
}
