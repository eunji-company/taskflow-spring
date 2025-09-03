package indiv.abko.taskflow.global.exception;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GlobalErrorCode implements ErrorCode {
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "알 수 없는 오류입니다. 관리자에게 연락해주세요.");

	private final HttpStatus httpStatus;
	private final String message;
}