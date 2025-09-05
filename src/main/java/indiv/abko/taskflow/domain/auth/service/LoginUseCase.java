package indiv.abko.taskflow.domain.auth.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import indiv.abko.taskflow.domain.auth.dto.command.LoginCommand;
import indiv.abko.taskflow.domain.auth.dto.response.LoginResponse;
import indiv.abko.taskflow.domain.auth.exception.AuthErrorCode;
import indiv.abko.taskflow.domain.auth.mapper.AuthMapper;
import indiv.abko.taskflow.domain.user.entity.Member;
import indiv.abko.taskflow.domain.user.service.MemberServiceApi;
import indiv.abko.taskflow.global.exception.BusinessException;
import indiv.abko.taskflow.global.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoginUseCase {

	private final MemberServiceApi memberService;
	private final AuthMapper authMapper;
	private final PasswordEncoder passwordEncoder;
	private final JwtUtil jwtUtil;

	public LoginResponse execute(LoginCommand command) {
		Optional<Member> findMember = memberService.findByUsername(command.username());
		Member member = findMember.orElseThrow(() -> new BusinessException(AuthErrorCode.LOGIN_FAILED));

		if (!passwordEncoder.matches(command.password(), member.getPassword())) {
			throw new BusinessException(AuthErrorCode.LOGIN_FAILED);
		}

		String accessToken = jwtUtil.createAccessToken(member.getId(), member.getUserRole());

		return authMapper.toLoginResponse(accessToken);
	}
}
