package indiv.abko.taskflow.domain.user.service;

import indiv.abko.taskflow.domain.user.entity.Member;

import java.util.Optional;

public interface MemberServiceApi {

	Member getByIdOrThrow(long memberId);

	Member createMember(String username, String password, String email, String name);

	boolean existsByUsername(String username);

	boolean existsByEmail(String email);

	// TODO: 구현해주세요 혜준님
	default boolean exstsById(long memberId) {
		//id가 존재한다면 true, 없다면 false
		return true;
	}

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
