package indiv.abko.taskflow.domain.task.exception;

import indiv.abko.taskflow.global.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum TaskErrorCode implements ErrorCode {

	TASK_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 ID의 작업을 찾을 수 없습니다.");

	private final HttpStatus httpStatus;
	private final String message;
}
