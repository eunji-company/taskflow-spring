package indiv.abko.taskflow.domain.auth.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import indiv.abko.taskflow.domain.auth.dto.command.LoginCommand;
import indiv.abko.taskflow.domain.auth.dto.response.LoginResponse;
import indiv.abko.taskflow.domain.auth.exception.AuthErrorCode;
import indiv.abko.taskflow.domain.auth.mapper.AuthMapper;
import indiv.abko.taskflow.domain.user.entity.Member;
import indiv.abko.taskflow.domain.user.entity.UserRole;
import indiv.abko.taskflow.domain.user.service.MemberServiceApi;
import indiv.abko.taskflow.global.exception.BusinessException;
import indiv.abko.taskflow.global.jwt.JwtUtil;

@ExtendWith(MockitoExtension.class)
class LoginUseCaseTest {

	@Mock
	private MemberServiceApi memberService;

	@Mock
	private AuthMapper authMapper;

	@Mock
	private PasswordEncoder passwordEncoder;

	@Mock
	private JwtUtil jwtUtil;

	@InjectMocks
	private LoginUseCase loginUseCase;

	@Test
	void 로그인에_성공한다() {
		// given
		LoginCommand command = new LoginCommand("testuser", "password");
		Member member = Member.of("testuser", "encodedPassword", "test@test.com", "Test User", UserRole.USER);
		String accessToken = "testAccessToken";
		LoginResponse loginResponse = new LoginResponse(accessToken);

		given(memberService.findByUsername(command.username())).willReturn(Optional.of(member));
		given(passwordEncoder.matches(command.password(), member.getPassword())).willReturn(true);
		given(jwtUtil.createAccessToken(member.getId(), member.getUserRole())).willReturn(accessToken);
		given(authMapper.toLoginResponse(accessToken)).willReturn(loginResponse);

		// when
		LoginResponse result = loginUseCase.execute(command);

		// then
		assertNotNull(result);
		assertEquals(accessToken, result.token());
		verify(memberService, times(1)).findByUsername(command.username());
		verify(passwordEncoder, times(1)).matches(command.password(), member.getPassword());
		verify(jwtUtil, times(1)).createAccessToken(member.getId(), member.getUserRole());
		verify(authMapper, times(1)).toLoginResponse(accessToken);
	}

	@Test
	void 존재하지_않는_아이디로_로그인에_실패한다() {
		// given
		LoginCommand command = new LoginCommand("nonexistentuser", "password");
		given(memberService.findByUsername(command.username())).willReturn(Optional.empty());

		// when & then
		BusinessException exception = assertThrows(BusinessException.class, () -> loginUseCase.execute(command));
		assertEquals(AuthErrorCode.LOGIN_FAILED, exception.getErrorCode());

		verify(memberService, times(1)).findByUsername(command.username());
		verify(passwordEncoder, never()).matches(anyString(), anyString());
		verify(jwtUtil, never()).createAccessToken(any(), any());
		verify(authMapper, never()).toLoginResponse(anyString());
	}

	@Test
	void 비밀번호가_틀려_로그인에_실패한다() {
		// given
		LoginCommand command = new LoginCommand("testuser", "wrongpassword");
		Member member = Member.of("testuser", "encodedPassword", "test@test.com", "Test User", UserRole.USER);

		given(memberService.findByUsername(command.username())).willReturn(Optional.of(member));
		given(passwordEncoder.matches(command.password(), member.getPassword())).willReturn(false);

		// when & then
		BusinessException exception = assertThrows(BusinessException.class, () -> loginUseCase.execute(command));
		assertEquals(AuthErrorCode.LOGIN_FAILED, exception.getErrorCode());

		verify(memberService, times(1)).findByUsername(command.username());
		verify(passwordEncoder, times(1)).matches(command.password(), member.getPassword());
		verify(jwtUtil, never()).createAccessToken(any(), any());
		verify(authMapper, never()).toLoginResponse(anyString());
	}
}