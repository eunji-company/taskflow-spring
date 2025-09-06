package indiv.abko.taskflow.domain.team.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import indiv.abko.taskflow.domain.team.dto.response.DeleteTeamMemberResponse;
import indiv.abko.taskflow.domain.team.entity.Team;
import indiv.abko.taskflow.domain.team.entity.TeamMember;
import indiv.abko.taskflow.domain.team.entity.TeamMemberId;
import indiv.abko.taskflow.domain.team.exception.TeamErrorCode;
import indiv.abko.taskflow.domain.team.repository.TeamMemberRepository;
import indiv.abko.taskflow.domain.team.repository.TeamRepository;
import indiv.abko.taskflow.domain.user.entity.Member;
import indiv.abko.taskflow.domain.user.service.MemberServiceApi;
import indiv.abko.taskflow.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeleteTeamMemberUseCase {
	private final TeamRepository teamRepository;
	private final TeamMemberRepository teamMemberRepository;
	private final MemberServiceApi memberServiceApi;

	// 팀 멤버 제거
	@Transactional
	public DeleteTeamMemberResponse execute(Long teamId, Long memberId) {
		Member member = memberServiceApi.getByIdOrThrow(memberId);

		Team team = teamRepository.findWithTeamMembersById(teamId).orElseThrow(
			() -> new BusinessException(TeamErrorCode.NOT_FOUND_TEAM)
		);

		TeamMemberId teamMemberId = new TeamMemberId(team, member);

		boolean exists = teamMemberRepository.existsById(teamMemberId);

		if (!exists) {
			throw new BusinessException(TeamErrorCode.NOT_TEAM_MEMBER);
		}

		team.deleteMember(member);

		List<DeleteTeamMemberResponse.UserResp> resps = new ArrayList<>();

		for (TeamMember teamMember : team.getTeamMembers()) {
			Member member1 = teamMember.getId().getMember();
			resps.add(new DeleteTeamMemberResponse.UserResp(
				member1.getId(),
				member1.getUsername(),
				member1.getName(),
				member1.getEmail(),
				member1.getUserRole().name(),
				member1.getCreatedAt()));
		}

		return new DeleteTeamMemberResponse(
			team.getId(),
			team.getName(),
			team.getDescription(),
			team.getCreatedAt(),
			resps);
	}
}
