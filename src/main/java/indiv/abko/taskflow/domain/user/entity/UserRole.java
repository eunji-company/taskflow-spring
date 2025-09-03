package indiv.abko.taskflow.domain.user.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserRole {
	USER("USER"),
	ADMIN("ADMIN");

	private final String key;
}
