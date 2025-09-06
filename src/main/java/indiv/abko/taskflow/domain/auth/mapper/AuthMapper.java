package indiv.abko.taskflow.domain.auth.mapper;

import org.springframework.stereotype.Component;

import indiv.abko.taskflow.domain.auth.dto.command.LoginCommand;
import indiv.abko.taskflow.domain.auth.dto.command.RegisterCommand;
import indiv.abko.taskflow.domain.auth.dto.command.WithdrawCommand;
import indiv.abko.taskflow.domain.auth.dto.request.LoginRequest;
import indiv.abko.taskflow.domain.auth.dto.request.RegisterRequest;
import indiv.abko.taskflow.domain.auth.dto.request.WithdrawRequest;
import indiv.abko.taskflow.domain.auth.dto.response.LoginResponse;
import indiv.abko.taskflow.domain.auth.dto.response.RegisterResponse;
import indiv.abko.taskflow.domain.user.entity.Member;
import indiv.abko.taskflow.global.auth.AuthMember;
import indiv.abko.taskflow.global.dto.DtoConstants;
import indiv.abko.taskflow.global.jwt.JwtUtil;

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

	public WithdrawCommand toWithdrawCommand(
		AuthMember authMember,
		WithdrawRequest request,
		String authorizationHeader
	) {

		// BEARER_PRIFIX 를 제거한 순수 토큰 값 추출
		String token = authorizationHeader.substring(JwtUtil.BEARER_PREFIX.length());

		return WithdrawCommand.builder()
			.memberId(authMember.memberId())
			.password(request.password())
			.token(token)
			.build();
	}
}
