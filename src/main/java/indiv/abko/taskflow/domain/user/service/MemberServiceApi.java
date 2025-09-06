package indiv.abko.taskflow.domain.user.service;

import java.util.Optional;

import indiv.abko.taskflow.domain.user.entity.Member;

public interface MemberServiceApi {

	Member getByIdOrThrow(long memberId);

	Member createMember(String username, String password, String email, String name);

	boolean existsByUsername(String username);

	boolean existsByEmail(String email);

	Optional<Member> findByUsername(String username);

	/*
	TODO: 혜준님 필독!
	MEMBER Entity에 @SQLDelete로 Soft delete 처리 해뒀으니
	그냥 memberRepository delete 메서드 쓰시면 됩니다!
	 */
	default void withdraw(Member member) {
		// member로 회원탈퇴 처리
	}
}
