package indiv.abko.taskflow.domain.auth.dto.response;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonFormat;

public record RegisterResponse(
	long id,
	String username,
	String email,
	String name,
	String role,
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
	Instant createdAt
) {

	public static RegisterResponse of(
		long id,
		String username,
		String email,
		String name,
		String role,
		Instant createdAt
	) {

		return new RegisterResponse(id, username, email, name, role, createdAt);
	}
}
