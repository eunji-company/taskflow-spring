package indiv.abko.taskflow.domain.task.exception;

import indiv.abko.taskflow.global.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum TaskErrorCode implements ErrorCode {

	TASK_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 ID의 작업을 찾을 수 없습니다."),
	MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
	TASK_LIST_EMPTY(HttpStatus.NOT_FOUND, "조회 가능한 작업이 존재하지 않습니다."),
	FORBIDDEN_TASK_CREATION(HttpStatus.FORBIDDEN, "본인만 작업을 생성할 수 있습니다."),
	TASK_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증에 실패했습니다.");

	private final HttpStatus httpStatus;
	private final String message;
}
