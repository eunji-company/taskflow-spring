package indiv.abko.taskflow.domain.auth.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import indiv.abko.taskflow.domain.auth.dto.command.LoginCommand;
import indiv.abko.taskflow.domain.auth.dto.command.RegisterCommand;
import indiv.abko.taskflow.domain.auth.dto.command.WithdrawCommand;
import indiv.abko.taskflow.domain.auth.dto.request.LoginRequest;
import indiv.abko.taskflow.domain.auth.dto.request.RegisterRequest;
import indiv.abko.taskflow.domain.auth.dto.request.WithdrawRequest;
import indiv.abko.taskflow.domain.auth.dto.response.LoginResponse;
import indiv.abko.taskflow.domain.auth.dto.response.RegisterResponse;
import indiv.abko.taskflow.domain.auth.mapper.AuthMapper;
import indiv.abko.taskflow.domain.auth.service.LoginUseCase;
import indiv.abko.taskflow.domain.auth.service.RegisterUseCase;
import indiv.abko.taskflow.domain.auth.service.WithdrawUseCase;
import indiv.abko.taskflow.global.auth.AuthMember;
import indiv.abko.taskflow.global.dto.CommonResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthController {

	private final RegisterUseCase registerUseCase;
	private final LoginUseCase loginUseCase;
	private final WithdrawUseCase withdrawUseCase;
	private final AuthMapper authMapper;

	@PostMapping("/auth/register")
	public CommonResponse<RegisterResponse> register(
		@Valid @RequestBody RegisterRequest request
	) {
		RegisterCommand command = authMapper.toRegisterCommand(request);
		RegisterResponse result = registerUseCase.execute(command);
		return CommonResponse.success("회원가입이 완료되었습니다.", result);
	}

	@PostMapping("/auth/login")
	public CommonResponse<LoginResponse> login(
		@Valid @RequestBody LoginRequest request
	) {
		LoginCommand command = authMapper.toLoginCommand(request);
		LoginResponse result = loginUseCase.execute(command);
		return CommonResponse.success("로그인이 완료되었습니다.", result);
	}

	@PostMapping("/auth/withdraw")
	public CommonResponse<Void> withdraw(
		@AuthenticationPrincipal AuthMember authMember,
		@Valid @RequestBody WithdrawRequest request,
		@RequestHeader("Authorization") String authorizationHeader
	) {
		WithdrawCommand command = authMapper.toWithdrawCommand(authMember, request, authorizationHeader);
		withdrawUseCase.execute(command);

		return CommonResponse.success("회원탈퇴가 완료되었습니다.", null);
	}

}
