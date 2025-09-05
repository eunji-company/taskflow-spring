package indiv.abko.taskflow.domain.auth.dto.command;

import lombok.Builder;

@Builder
public record LoginCommand(
	String username,
	String password
) {
}
