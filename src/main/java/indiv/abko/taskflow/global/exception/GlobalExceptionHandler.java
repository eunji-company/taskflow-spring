package indiv.abko.taskflow.global.exception;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import indiv.abko.taskflow.domain.auth.exception.AuthErrorCode;
import indiv.abko.taskflow.global.dto.CommonResponse;
import jakarta.validation.ConstraintViolationException;
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

	@ExceptionHandler(AuthenticationException.class)
	public ResponseEntity<CommonResponse<?>> handleAuthenticationException(AuthenticationException ex) {
		ErrorCode errorCode;
		if (ex.getCause() instanceof BusinessException businessException) {
			errorCode = businessException.getErrorCode();
		} else {
			errorCode = AuthErrorCode.AUTHENTICATION_FAILED;
		}

		CommonResponse<?> response = CommonResponse.failure(errorCode.getMessage(), null);
		HttpStatus status = errorCode.getHttpStatus();

		return new ResponseEntity<>(response, status);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<CommonResponse<?>> handleValidationException(MethodArgumentNotValidException ex) {
		Map<String, String> errors = ex.getBindingResult()
			.getFieldErrors()
			.stream()
			.collect(Collectors.toMap(
				FieldError::getField,
				FieldError::getDefaultMessage));

		return new ResponseEntity<>(CommonResponse.failure("잘못된 요청입니다.", errors), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<CommonResponse<?>> handleConstraintViolationException(ConstraintViolationException ex) {
		Map<String, String> errors = ex.getConstraintViolations()
			.stream()
			.collect(Collectors.toMap(
				violation -> violation.getPropertyPath().toString(),
				violation -> violation.getMessage()
			));

		return new ResponseEntity<>(CommonResponse.failure("잘못된 요청입니다.", errors), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<CommonResponse<?>> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
		return new ResponseEntity<>(CommonResponse.failure("잘못된 요청입니다.", null), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<CommonResponse<?>> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
		String param = ex.getName();
		String requiredType = ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "Unknown";
		String message = String.format("%s 파라미터의 타입이 유효하지 않습니다. 기대 타입: %s", param, requiredType);
		Map<String, String> errors = Map.of(param, message);
		return new ResponseEntity<>(CommonResponse.failure("잘못된 요청입니다.", errors), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<CommonResponse<?>> handleHttpRequestMethodNotSupported(
		HttpRequestMethodNotSupportedException ex) {
		String method = ex.getMethod();
		String supported = ex.getSupportedHttpMethods() != null
			? ex.getSupportedHttpMethods().stream().map(HttpMethod::name).collect(Collectors.joining(", "))
			: "Unknown";
		String message = String.format("지원되지 않는 HTTP 메서드입니다: %s. 허용된 메서드: %s", method, supported);
		Map<String, String> errors = Map.of("method", message);
		return new ResponseEntity<>(CommonResponse.failure("잘못된 요청입니다.", errors), HttpStatus.METHOD_NOT_ALLOWED);
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<CommonResponse<?>> handleAccessDeniedException(AccessDeniedException ex) {
		ErrorCode errorCode = AuthErrorCode.FORBIDDEN;
		HttpStatus status = errorCode.getHttpStatus();
		String message = errorCode.getMessage();
		return new ResponseEntity<>(CommonResponse.failure(message, null), status);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<CommonResponse<?>> handleException(Exception ex) {
		log.error("Unhandled exception: {}", ex.getMessage(), ex);
		HttpStatus status = GlobalErrorCode.INTERNAL_SERVER_ERROR.getHttpStatus();
		String message = GlobalErrorCode.INTERNAL_SERVER_ERROR.getMessage();
		return new ResponseEntity<>(CommonResponse.failure(message, status), status);
	}
}
