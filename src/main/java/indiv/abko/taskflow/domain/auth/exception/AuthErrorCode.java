package indiv.abko.taskflow.domain.auth.exception;

import org.springframework.http.HttpStatus;

import indiv.abko.taskflow.global.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AuthErrorCode implements ErrorCode {
	INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),
	AUTHENTICATION_FAILED(HttpStatus.UNAUTHORIZED, "인증에 실패했습니다."),
	DUPLICATE_USERNAME(HttpStatus.BAD_REQUEST, "이미 존재하는 아이디입니다."),
	DUPLICATE_EMAIL(HttpStatus.BAD_REQUEST, "이미 존재하는 이메일입니다.");

	private final HttpStatus httpStatus;
	private final String message;
}
