package indiv.abko.taskflow.domain.comment.exception;

import org.springframework.http.HttpStatus;

import indiv.abko.taskflow.global.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CommentErrorCode implements ErrorCode {
	COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 댓글이 존재하지 않습니다"),
	COMMENT_FORBIDDEN(HttpStatus.FORBIDDEN, "댓글에 대한 권한이 없습니다");

	private final HttpStatus httpStatus;
	private final String message;
}
