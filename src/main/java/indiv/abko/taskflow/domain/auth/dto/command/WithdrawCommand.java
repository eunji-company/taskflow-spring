package indiv.abko.taskflow.domain.auth.dto.command;

import lombok.Builder;

@Builder
public record WithdrawCommand(
	long memberId,
	String password,
	String token
) {
}
