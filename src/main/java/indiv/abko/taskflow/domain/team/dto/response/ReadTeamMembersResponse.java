package indiv.abko.taskflow.domain.team.dto.response;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonFormat;

import indiv.abko.taskflow.global.dto.DtoConstants;

public record ReadTeamMembersResponse(
	long id,
	String username,
	String name,
	String email,
	String role,
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DtoConstants.TIME_FORMAT, timezone = DtoConstants.DISPLAY_TIME_ZONE_STRING)
	Instant createdAt) {
}
