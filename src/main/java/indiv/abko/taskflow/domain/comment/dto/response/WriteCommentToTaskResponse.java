package indiv.abko.taskflow.domain.comment.dto.response;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonFormat;

import indiv.abko.taskflow.global.dto.DtoConstants;

public record WriteCommentToTaskResponse(
	long id,
	String content,
	Long taskId,
	long userId,
	UserResp user,
	Long parentId,
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DtoConstants.TIME_FORMAT, timezone = DtoConstants.DISPLAY_TIME_ZONE_STRING)
	Instant createdAt,
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DtoConstants.TIME_FORMAT, timezone = DtoConstants.DISPLAY_TIME_ZONE_STRING)
	Instant updatedAt) {
	public record UserResp(
		long id,
		String username,
		String name,
		String email,
		String role) {
	}
}
