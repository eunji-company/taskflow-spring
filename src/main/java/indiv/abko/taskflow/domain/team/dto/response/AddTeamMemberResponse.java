package indiv.abko.taskflow.domain.team.dto.response;

import java.time.Instant;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import indiv.abko.taskflow.global.dto.DtoConstants;

public record AddTeamMemberResponse(
	Long id,
	String name,
	String description,
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DtoConstants.TIME_FORMAT, timezone = DtoConstants.DISPLAY_TIME_ZONE_STRING)
	Instant createdAt,
	List<UserResp> members
) {
	public record UserResp(
		long id,
		String username,
		String name,
		String email,
		String role,
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DtoConstants.TIME_FORMAT, timezone = DtoConstants.DISPLAY_TIME_ZONE_STRING)
		Instant createdAt) {
	}
}