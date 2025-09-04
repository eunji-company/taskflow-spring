package indiv.abko.taskflow.domain.team.exception;

import org.springframework.http.HttpStatus;

import indiv.abko.taskflow.global.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TeamErrorCode implements ErrorCode {
	DUPLICATE_TEAM_NAME(HttpStatus.BAD_REQUEST, "팀 이름이 이미 존재합니다.");

	private final HttpStatus httpStatus;
	private final String message;
}
