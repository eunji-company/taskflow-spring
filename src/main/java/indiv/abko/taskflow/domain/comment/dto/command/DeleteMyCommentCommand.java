package indiv.abko.taskflow.domain.comment.dto.command;

public record DeleteMyCommentCommand(
	long requesterId,
	long targetCommentId
) {
}
