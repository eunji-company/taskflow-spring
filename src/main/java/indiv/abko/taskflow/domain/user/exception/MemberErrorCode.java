package indiv.abko.taskflow.domain.user.exception;

import org.springframework.http.HttpStatus;

import indiv.abko.taskflow.global.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MemberErrorCode implements ErrorCode {
	MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다");

	private final HttpStatus httpStatus;
	private final String message;
}