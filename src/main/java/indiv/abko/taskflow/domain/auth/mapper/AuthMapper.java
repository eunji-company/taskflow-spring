package indiv.abko.taskflow.domain.auth.mapper;

import org.springframework.stereotype.Component;

import indiv.abko.taskflow.domain.auth.dto.command.LoginCommand;
import indiv.abko.taskflow.domain.auth.dto.command.RegisterCommand;
import indiv.abko.taskflow.domain.auth.dto.request.LoginRequest;
import indiv.abko.taskflow.domain.auth.dto.request.RegisterRequest;
import indiv.abko.taskflow.domain.auth.dto.response.LoginResponse;
import indiv.abko.taskflow.domain.auth.dto.response.RegisterResponse;
import indiv.abko.taskflow.domain.user.entity.Member;
import indiv.abko.taskflow.global.dto.DtoConstants;

@Component
public class AuthMapper {

	public RegisterResponse toRegisterResponse(Member member) {
		return RegisterResponse.builder()
			.id(member.getId())
			.username(member.getUsername())
			.email(member.getEmail())
			.name(member.getName())
			.role(member.getUserRole().name())
			.createdAt(member.getCreatedAt().toInstant(DtoConstants.REAL_TIME_ZONE_OFFSET))
			.build();
	}

	public RegisterCommand toRegisterCommand(RegisterRequest request) {
		return RegisterCommand.builder()
			.username(request.username())
			.email(request.email())
			.password(request.password())
			.name(request.name())
			.build();
	}

	public LoginCommand toLoginCommand(LoginRequest request) {
		return LoginCommand.builder()
			.username(request.username())
			.password(request.password())
			.build();
	}

	public LoginResponse toLoginResponse(String accessToken) {
		return LoginResponse.builder()
			.token(accessToken)
			.build();
	}
}
