package indiv.abko.taskflow.domain.auth.dto.request;

import indiv.abko.taskflow.domain.auth.constant.PatternConstants;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
	@NotBlank(message = "아이디를 입력해주세요.")
	@Size(min = 4, max = 20, message = "아이디는 4~20자 이내로 입력해주세요.")
	@Pattern(regexp = PatternConstants.USERNAME_PATTERN, message = "아이디는 영문/숫자만 입력해주세요.")
	String username,

	@NotBlank(message = "비밀번호를 입력해주세요.")
	@Pattern(regexp = PatternConstants.PASSWORD_PATTERN, message = "비밀번호는 영문/숫자/특수문자 조합으로 8자 이상 입력해주세요.")
	String password,

	@NotBlank(message = "이메일을 입력해주세요.")
	@Email
	String email,

	@NotBlank(message = "이름을 입력해주세요.")
	@Size(min = 2, max = 50, message = "이름은 2~50자 이내로 입력해주세요.")
	String name
) {
}