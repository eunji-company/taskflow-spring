package indiv.abko.taskflow.domain.user.service;

import indiv.abko.taskflow.domain.user.entity.Member;

public interface MemberServiceApi {
	Member getByIdOrThrow(long memberId);
}
