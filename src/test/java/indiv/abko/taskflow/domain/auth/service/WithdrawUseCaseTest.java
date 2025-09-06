package indiv.abko.taskflow.domain.auth.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import indiv.abko.taskflow.domain.auth.dto.command.WithdrawCommand;
import indiv.abko.taskflow.domain.auth.exception.AuthErrorCode;
import indiv.abko.taskflow.domain.user.entity.Member;
import indiv.abko.taskflow.domain.user.entity.UserRole;
import indiv.abko.taskflow.domain.user.exception.MemberErrorCode;
import indiv.abko.taskflow.domain.user.service.MemberServiceApi;
import indiv.abko.taskflow.global.exception.BusinessException;
import indiv.abko.taskflow.global.jwt.JwtBlacklistService;

@ExtendWith(MockitoExtension.class)
class WithdrawUseCaseTest {

	@Mock
	private MemberServiceApi memberService;

	@Mock
	private PasswordEncoder passwordEncoder;

	@Mock
	private JwtBlacklistService jwtBlacklistService;

	@InjectMocks
	private WithdrawUseCase withdrawUseCase;

	@Test
	void 회원탈퇴에_성공한다() {
		// given
		WithdrawCommand command = new WithdrawCommand(1L, "password123!", "testAccessToken");
		Member member = Member.of("testuser", "encodedPassword", "test@test.com", "testName", UserRole.USER);

		given(memberService.getByIdOrThrow(command.memberId())).willReturn(member);
		given(passwordEncoder.matches(command.password(), member.getPassword())).willReturn(true);
		willDoNothing().given(jwtBlacklistService).addToBlacklist(command.token());
		willDoNothing().given(memberService).withdraw(member);

		// when
		withdrawUseCase.execute(command);

		// then
		then(memberService).should().getByIdOrThrow(command.memberId());
		then(passwordEncoder).should().matches(command.password(), member.getPassword());
		then(jwtBlacklistService).should().addToBlacklist(command.token());
		then(memberService).should().withdraw(member);
	}

	@Test
	void 회원을_찾을_수_없어_회원탈퇴에_실패한다() {
		// given
		WithdrawCommand command = new WithdrawCommand(1L, "password123!", "testAccessToken");
		BusinessException memberNotFoundException = new BusinessException(MemberErrorCode.MEMBER_NOT_FOUND);
		given(memberService.getByIdOrThrow(command.memberId())).willThrow(memberNotFoundException);

		// when & then
		assertThatThrownBy(() -> withdrawUseCase.execute(command))
			.isInstanceOf(BusinessException.class)
			.hasMessage(MemberErrorCode.MEMBER_NOT_FOUND.getMessage());

		then(memberService).should().getByIdOrThrow(command.memberId());
		then(passwordEncoder).should(never()).matches(anyString(), anyString());
		then(jwtBlacklistService).should(never()).addToBlacklist(anyString());
		then(memberService).should(never()).withdraw(any(Member.class));
	}

	@Test
	void 비밀번호가_일치하지_않아_회원탈퇴에_실패한다() {
		// given
		WithdrawCommand command = new WithdrawCommand(1L, "password123!", "testAccessToken");
		Member member = Member.of("testuser", "encodedPassword", "test@test.com", "testName", UserRole.USER);

		given(memberService.getByIdOrThrow(command.memberId())).willReturn(member);
		given(passwordEncoder.matches(command.password(), member.getPassword())).willReturn(false);

		// when & then
		assertThatThrownBy(() -> withdrawUseCase.execute(command))
			.isInstanceOf(BusinessException.class)
			.hasMessage(AuthErrorCode.PASSWORD_MISMATCH.getMessage());

		then(memberService).should().getByIdOrThrow(command.memberId());
		then(passwordEncoder).should().matches(command.password(), member.getPassword());
		then(jwtBlacklistService).should(never()).addToBlacklist(anyString());
		then(memberService).should(never()).withdraw(any(Member.class));
	}
}