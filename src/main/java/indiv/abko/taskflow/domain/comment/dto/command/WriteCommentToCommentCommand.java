package indiv.abko.taskflow.domain.comment.dto.command;

public record WriteCommentToCommentCommand(
    long authorId,
    long taskId,
    long parentCommentId,
    String content) {

}
