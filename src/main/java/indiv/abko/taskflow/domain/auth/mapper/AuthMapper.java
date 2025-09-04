package indiv.abko.taskflow.domain.auth.mapper;

import java.time.ZoneOffset;

import org.springframework.stereotype.Component;

import indiv.abko.taskflow.domain.auth.dto.command.RegisterCommand;
import indiv.abko.taskflow.domain.auth.dto.request.RegisterRequest;
import indiv.abko.taskflow.domain.auth.dto.response.RegisterResponse;
import indiv.abko.taskflow.domain.user.entity.Member;

@Component
public class AuthMapper {

	public RegisterResponse toRegisterResponse(Member member) {
		return RegisterResponse.of(
			member.getId(),
			member.getUsername(),
			member.getEmail(),
			member.getName(),
			member.getUserRole().name(),
			member.getCreatedAt().toInstant(ZoneOffset.ofHours(9))
		);
	}

	public RegisterCommand toRegisterCommand(RegisterRequest request) {
		return RegisterCommand.of(
			request.username(),
			request.email(),
			request.password(),
			request.name()
		);
	}
}
