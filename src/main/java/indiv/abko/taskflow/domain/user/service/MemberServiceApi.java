package indiv.abko.taskflow.domain.user.service;

import indiv.abko.taskflow.domain.user.entity.Member;

public interface MemberServiceApi {
	// TODO 혜준님~ 해주세요~
	default Member getByIdOrThrow(long memberId) {
		// 기본키를 사용하여 Member를 가져온다
		// 없으면 예외처리
		return null;
	}
}
