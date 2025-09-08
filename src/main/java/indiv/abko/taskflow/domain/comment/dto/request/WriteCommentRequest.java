package indiv.abko.taskflow.domain.comment.dto.request;

import jakarta.validation.constraints.NotBlank;

public record WriteCommentRequest(
	@NotBlank(message = "댓글 내용을 작성해주세요.")
	String content,
	Long parentId
) {
}
