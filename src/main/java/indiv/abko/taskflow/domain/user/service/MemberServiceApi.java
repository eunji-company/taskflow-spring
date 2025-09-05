package indiv.abko.taskflow.domain.user.service;

import indiv.abko.taskflow.domain.user.entity.Member;

import static indiv.abko.taskflow.domain.user.entity.UserRole.ADMIN;

public interface MemberServiceApi {

	Member getByIdOrThrow(long id);

	Member createMember(String username, String password, String email, String name);

	boolean existsByUsername(String username);

	boolean existsByEmail(String email);
}
