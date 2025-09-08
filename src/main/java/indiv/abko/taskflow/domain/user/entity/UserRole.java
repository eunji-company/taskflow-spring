package indiv.abko.taskflow.domain.user.entity;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum UserRole {
	USER("USER"),
	ADMIN("ADMIN");

	private final String key;

	public String getKey() {
		return "ROLE_" + key;
	}
}
