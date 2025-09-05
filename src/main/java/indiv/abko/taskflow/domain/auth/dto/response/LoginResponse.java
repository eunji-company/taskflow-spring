package indiv.abko.taskflow.domain.auth.dto.response;

import lombok.Builder;

@Builder
public record LoginResponse(
	String token
) {
}
