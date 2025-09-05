package indiv.abko.taskflow.domain.auth.dto.response;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Builder;

@Builder
public record RegisterResponse(
	long id,
	String username,
	String email,
	String name,
	String role,
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
	Instant createdAt
) {
}
