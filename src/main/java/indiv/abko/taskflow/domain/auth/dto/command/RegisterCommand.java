package indiv.abko.taskflow.domain.auth.dto.command;

public record RegisterCommand(
	String username,
	String email,
	String password,
	String name
) {

	public static RegisterCommand of(
		String username,
		String email,
		String password,
		String name
	) {
		
		return new RegisterCommand(username, email, password, name);
	}
}
