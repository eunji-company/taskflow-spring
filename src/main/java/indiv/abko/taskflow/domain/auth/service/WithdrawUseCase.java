package indiv.abko.taskflow.domain.auth.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import indiv.abko.taskflow.domain.auth.dto.command.WithdrawCommand;
import indiv.abko.taskflow.domain.auth.exception.AuthErrorCode;
import indiv.abko.taskflow.domain.user.entity.Member;
import indiv.abko.taskflow.domain.user.service.MemberServiceApi;
import indiv.abko.taskflow.global.exception.BusinessException;
import indiv.abko.taskflow.global.jwt.JwtBlacklistService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WithdrawUseCase {

	private final MemberServiceApi memberService;
	private final PasswordEncoder passwordEncoder;
	private final JwtBlacklistService jwtBlacklistService;

	public void execute(WithdrawCommand command) {
		Member member = memberService.getByIdOrThrow(command.memberId());

		if (!passwordEncoder.matches(command.password(), member.getPassword())) {
			throw new BusinessException(AuthErrorCode.PASSWORD_MISMATCH);
		}

		jwtBlacklistService.addToBlacklist(command.token());

		memberService.withdraw(member);
	}
}
