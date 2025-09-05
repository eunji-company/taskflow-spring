package indiv.abko.taskflow.domain.auth.dto.command;

import lombok.Builder;

@Builder
public record RegisterCommand(
	String username,
	String password,
	String email,
	String name
) {
}
