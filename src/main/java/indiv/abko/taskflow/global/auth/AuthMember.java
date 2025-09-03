package indiv.abko.taskflow.global.auth;

import indiv.abko.taskflow.domain.user.entity.UserRole;

public record AuthMember(
	long memberId,
	UserRole userRole
) {
}
