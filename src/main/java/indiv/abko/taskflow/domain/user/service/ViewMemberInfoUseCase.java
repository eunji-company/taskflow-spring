package indiv.abko.taskflow.domain.user.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import indiv.abko.taskflow.domain.user.dto.MemberInfoResponse;
import indiv.abko.taskflow.domain.user.entity.Member;
import indiv.abko.taskflow.domain.user.exception.MemberErrorCode;
import indiv.abko.taskflow.domain.user.repository.MemberRepository;
import indiv.abko.taskflow.global.dto.DtoConstants;
import indiv.abko.taskflow.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ViewMemberInfoUseCase {
	private final MemberRepository memberRepository;

	@Transactional(readOnly = true)
	public MemberInfoResponse execute(Long memberId) {
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new BusinessException(MemberErrorCode.MEMBER_NOT_FOUND));
		return new MemberInfoResponse(member.getId(), member.getUsername(), member.getEmail(), member.getName(),
			member.getUserRole().name(), member.getCreatedAt().toInstant(DtoConstants.REAL_TIME_ZONE_OFFSET));
	}
}
