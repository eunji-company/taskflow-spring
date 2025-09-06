package indiv.abko.taskflow.domain.user.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import indiv.abko.taskflow.domain.team.entity.Team;
import indiv.abko.taskflow.domain.team.service.TeamServiceApi;
import indiv.abko.taskflow.domain.user.dto.MemberInfoResponse;
import indiv.abko.taskflow.domain.user.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ViewAvailableMembersInfoUseCase {
	private final MemberRepository memberRepository;
	private final TeamServiceApi teamService;

	@Transactional(readOnly = true)
	public List<MemberInfoResponse> execute(Long teamId) {
		Team team = teamService.getByIdOrThrow(teamId);

		return memberRepository.findAvailableMembersForTeam(team)
			.stream()
			.map(m -> new MemberInfoResponse(m.getId(), m.getUsername(), m.getEmail(),
				m.getName(), m.getUserRole().name(), m.getCreatedAt()))
			.toList();
	}
}
