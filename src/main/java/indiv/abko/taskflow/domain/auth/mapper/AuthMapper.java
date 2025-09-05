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
		return RegisterResponse.builder()
			.id(member.getId())
			.username(member.getUsername())
			.email(member.getEmail())
			.name(member.getName())
			.role(member.getUserRole().name())
			.createdAt(member.getCreatedAt().toInstant(ZoneOffset.ofHours(9)))
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
}
