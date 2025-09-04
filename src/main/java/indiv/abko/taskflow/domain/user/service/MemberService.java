package indiv.abko.taskflow.domain.user.service;

import org.springframework.stereotype.Service;

import indiv.abko.taskflow.domain.user.entity.Member;
import indiv.abko.taskflow.domain.user.exception.MemberErrorCode;
import indiv.abko.taskflow.domain.user.repository.MemberRepository;
import indiv.abko.taskflow.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService implements MemberServiceApi {
	private final MemberRepository memberRepository;

	public Member getByIdOrThrow(long memberId) {
		return memberRepository.findById(memberId)
			.orElseThrow(() -> new BusinessException(MemberErrorCode.MEMBER_NOT_FOUND));
	}
}
