package indiv.abko.taskflow.domain.comment.dto.command;

public record WriteCommentToTaskCommand(
	long memberId,
	long taskId,
	String content
) {
}
