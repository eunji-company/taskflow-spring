package indiv.abko.taskflow.domain.user.service;

import org.springframework.stereotype.Service;

import indiv.abko.taskflow.domain.user.dto.MemberInfoResponse;
import indiv.abko.taskflow.domain.user.entity.Member;
import indiv.abko.taskflow.domain.user.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ViewMemberInfoUseCase {
	private final MemberRepository memberRepository;

	public MemberInfoResponse execute(Long memberId) {
		Member member = memberRepository.findByIdOrElseThrow(memberId);
		return MemberInfoResponse.from(member);
	}
}
