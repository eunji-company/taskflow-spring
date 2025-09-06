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
	PASSWORD_MISMATCH(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
	DUPLICATE_USERNAME(HttpStatus.BAD_REQUEST, "이미 존재하는 아이디입니다."),
	DUPLICATE_EMAIL(HttpStatus.BAD_REQUEST, "이미 존재하는 이메일입니다."),
	LOGIN_FAILED(HttpStatus.UNAUTHORIZED, "잘못된 사용자명 또는 비밀번호입니다.");

	private final HttpStatus httpStatus;
	private final String message;
}
