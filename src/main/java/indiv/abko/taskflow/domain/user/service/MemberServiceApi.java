package indiv.abko.taskflow.domain.user.service;

import indiv.abko.taskflow.domain.user.entity.Member;

public interface MemberServiceApi {

	Member getByIdOrThrow(long memberId);

	Member createMember(String username, String password, String email, String name);

	boolean existsByUsername(String username);

	boolean existsByEmail(String email);
}
