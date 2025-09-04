package indiv.abko.taskflow.domain.user.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import indiv.abko.taskflow.domain.team.service.TeamServiceApi;
import indiv.abko.taskflow.domain.user.dto.MemberInfoResponse;
import indiv.abko.taskflow.domain.user.exception.MemberErrorCode;
import indiv.abko.taskflow.domain.user.repository.MemberRepository;
import indiv.abko.taskflow.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ViewAvailableMembersInfoUseCase {
	private final MemberRepository memberRepository;
	private final TeamServiceApi teamService;

	@Transactional(readOnly = true)
	public List<MemberInfoResponse> execute(Long teamId, Long memberId) {
		memberRepository.findById(memberId)
			.orElseThrow(() -> new BusinessException(MemberErrorCode.MEMBER_NOT_FOUND));

		teamService.existsById(teamId);

		teamService.existsMemberInTeam(teamId, memberId);

		return teamService.getAvailableMembersForTeam(teamId);
	}
}
