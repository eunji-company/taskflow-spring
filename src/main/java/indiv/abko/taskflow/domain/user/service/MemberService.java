package indiv.abko.taskflow.domain.user.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import indiv.abko.taskflow.domain.user.entity.Member;
import indiv.abko.taskflow.domain.user.entity.UserRole;
import indiv.abko.taskflow.domain.user.exception.MemberErrorCode;
import indiv.abko.taskflow.domain.user.repository.MemberRepository;
import indiv.abko.taskflow.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService implements MemberServiceApi {
	private final MemberRepository memberRepository;

	@Override
	@Transactional(readOnly = true)
	public Member getByIdOrThrow(long memberId) {
		return memberRepository.findById(memberId)
			.orElseThrow(() -> new BusinessException(MemberErrorCode.MEMBER_NOT_FOUND));
	}

	@Override
	@Transactional
	public Member createMember(String username, String password, String email, String name) {
		Member member = Member.of(username, password, email, name, UserRole.USER);
		return memberRepository.save(member);
	}

	@Override
	@Transactional(readOnly = true)
	public boolean existsByUsername(String username) {
		return memberRepository.existsByUsername(username);
	}

	@Override
	@Transactional(readOnly = true)
	public boolean existsByEmail(String email) {
		return memberRepository.existsByEmail(email);
	}
}
