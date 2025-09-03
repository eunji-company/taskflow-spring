package indiv.abko.taskflow.global.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import indiv.abko.taskflow.global.dto.CommonResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<CommonResponse<?>> handleBusinessException(BusinessException ex) {
		CommonResponse<?> response = CommonResponse.failure(ex.getMessage(), null);
		HttpStatus status = ex.getErrorCode().getHttpStatus();

		return new ResponseEntity<>(response, status);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<CommonResponse<?>> handleException(Exception ex) {
		log.error("Unhandled exception: {}", ex.getMessage(), ex);
		HttpStatus status = GlobalErrorCode.INTERNAL_SERVER_ERROR.getHttpStatus();
		String message = GlobalErrorCode.INTERNAL_SERVER_ERROR.getMessage();
		return new ResponseEntity<>(CommonResponse.failure(message, status), status);
	}
}
