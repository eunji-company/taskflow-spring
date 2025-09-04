package indiv.abko.taskflow.global.dto;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonFormat;

public record CommonResponse<T>(
	boolean success,
	String message,
	T data,
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DtoConstants.TIME_FORMAT, timezone = DtoConstants.TIME_ZONE_STRING)
	Instant timestamp) {
	public static <T> CommonResponse<T> success(String message, T data) {
		return new CommonResponse<>(
			true,
			message,
			data,
			Instant.now());
	}

	public static <T> CommonResponse<T> failure(String message, T data) {
		return new CommonResponse<>(
			false,
			message,
			data,
			Instant.now());
	}
}
