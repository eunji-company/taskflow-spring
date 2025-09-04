package indiv.abko.taskflow.domain.auth.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import indiv.abko.taskflow.domain.auth.dto.command.RegisterCommand;
import indiv.abko.taskflow.domain.auth.dto.response.RegisterResponse;
import indiv.abko.taskflow.domain.auth.exception.AuthErrorCode;
import indiv.abko.taskflow.domain.auth.mapper.AuthMapper;
import indiv.abko.taskflow.domain.user.entity.Member;
import indiv.abko.taskflow.domain.user.service.MemberServiceApi;
import indiv.abko.taskflow.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RegisterUseCase {

	private final MemberServiceApi memberService;
	private final PasswordEncoder passwordEncoder;
	private final AuthMapper authMapper;

	@Transactional
	public RegisterResponse execute(RegisterCommand command) {
		if (memberService.existsByUsername(command.username())) {
			throw new BusinessException(AuthErrorCode.DUPLICATE_USERNAME);
		}

		if (memberService.existsByEmail(command.email())) {
			throw new BusinessException(AuthErrorCode.DUPLICATE_EMAIL);
		}

		String encodedPassword = passwordEncoder.encode(command.password());

		Member member = memberService.createMember(
			command.username(),
			encodedPassword,
			command.email(),
			command.name()
		);

		return authMapper.toRegisterResponse(member);
	}
}
