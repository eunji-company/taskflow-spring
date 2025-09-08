package indiv.abko.taskflow.domain.auth.dto.response;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonFormat;

import indiv.abko.taskflow.global.dto.DtoConstants;
import lombok.Builder;

@Builder
public record RegisterResponse(
	long id,
	String username,
	String email,
	String name,
	String role,
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DtoConstants.TIME_FORMAT, timezone = DtoConstants.DISPLAY_TIME_ZONE_STRING)
	Instant createdAt
) {
}
