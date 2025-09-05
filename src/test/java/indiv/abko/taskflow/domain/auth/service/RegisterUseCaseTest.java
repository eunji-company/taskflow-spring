package indiv.abko.taskflow.domain.auth.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import indiv.abko.taskflow.domain.auth.dto.command.RegisterCommand;
import indiv.abko.taskflow.domain.auth.dto.response.RegisterResponse;
import indiv.abko.taskflow.domain.auth.exception.AuthErrorCode;
import indiv.abko.taskflow.domain.auth.mapper.AuthMapper;
import indiv.abko.taskflow.domain.user.entity.Member;
import indiv.abko.taskflow.domain.user.entity.UserRole;
import indiv.abko.taskflow.domain.user.service.MemberServiceApi;
import indiv.abko.taskflow.global.exception.BusinessException;

@ExtendWith(MockitoExtension.class)
public class RegisterUseCaseTest {

	@Mock
	private MemberServiceApi memberService;

	@Mock
	private PasswordEncoder passwordEncoder;

	@Mock
	private AuthMapper authMapper;

	@InjectMocks
	private RegisterUseCase registerUseCase;

	@Test
	void 회원가입에_성공한다() {
		// given
		RegisterCommand command = RegisterCommand.builder()
			.username("testuser")
			.password("password")
			.email("test@test.com")
			.name("Test User")
			.build();

		String encodedPassword = "encodedPassword";

		Member member = Member.of(
			command.username(),
			encodedPassword,
			command.email(),
			command.name(),
			UserRole.USER
		);

		RegisterResponse response = RegisterResponse.builder()
			.id(1L)
			.username(command.username())
			.email(command.email())
			.name(command.name())
			.role(UserRole.USER.name())
			.createdAt(LocalDateTime.now().toInstant(ZoneOffset.ofHours(9)))
			.build();

		given(memberService.existsByUsername(command.username())).willReturn(false);
		given(memberService.existsByEmail(command.email())).willReturn(false);
		given(passwordEncoder.encode(command.password())).willReturn(encodedPassword);
		given(memberService.createMember(command.username(), encodedPassword, command.email(), command.name()))
			.willReturn(member);
		given(authMapper.toRegisterResponse(member)).willReturn(response);

		// when
		RegisterResponse result = registerUseCase.execute(command);

		// then
		assertNotNull(result);
		assertEquals(response.username(), result.username());
		assertEquals(response.email(), result.email());
		assertEquals(response.name(), result.name());
		assertEquals(response.role(), result.role());
		assertEquals(response.createdAt(), result.createdAt());
		verify(memberService, times(1)).existsByUsername(command.username());
		verify(memberService, times(1)).existsByEmail(command.email());
		verify(passwordEncoder, times(1)).encode(command.password());
		verify(memberService, times(1)).createMember(command.username(), encodedPassword, command.email(),
			command.name());
		verify(authMapper, times(1)).toRegisterResponse(member);
	}

	@Test
	void 중복된_아이디로_회원가입에_실패한다() {
		// given
		RegisterCommand command = RegisterCommand.builder()
			.username("testuser")
			.password("password")
			.email("test@test.com")
			.name("Test User")
			.build();

		given(memberService.existsByUsername(command.username())).willReturn(true);

		// when & then
		BusinessException exception = assertThrows(BusinessException.class, () -> registerUseCase.execute(command));
		assertEquals(AuthErrorCode.DUPLICATE_USERNAME, exception.getErrorCode());

		verify(memberService, times(1)).existsByUsername(command.username());
		verify(memberService, never()).existsByEmail(anyString());
		verify(passwordEncoder, never()).encode(anyString());
		verify(memberService, never()).createMember(anyString(), anyString(), anyString(), anyString());
	}

	@Test
	void 중복된_이메일로_회원가입에_실패한다() {
		// given
		RegisterCommand command = RegisterCommand.builder()
			.username("testuser")
			.password("password")
			.email("test@test.com")
			.name("Test User")
			.build();

		given(memberService.existsByUsername(command.username())).willReturn(false);
		given(memberService.existsByEmail(command.email())).willReturn(true);

		// when & then
		BusinessException exception = assertThrows(BusinessException.class, () -> registerUseCase.execute(command));
		assertEquals(AuthErrorCode.DUPLICATE_EMAIL, exception.getErrorCode());

		verify(memberService, times(1)).existsByUsername(command.username());
		verify(memberService, times(1)).existsByEmail(command.email());
		verify(passwordEncoder, never()).encode(anyString());
		verify(memberService, never()).createMember(anyString(), anyString(), anyString(), anyString());
	}
}
