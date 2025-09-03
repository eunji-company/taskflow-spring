package indiv.abko.taskflow.domain.comment.dto.response;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonFormat;

public record WriteCommentToTaskResponse(
	long id,
	String content,
	Long taskId,
	long userId,
	UserResp user,
	Long parentId,
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
	Instant createdAt,
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
	Instant updatedAt
) {
	public record UserResp(
		long id,
		String username,
		String name,
		String email,
		String role
	) {
	}
}
