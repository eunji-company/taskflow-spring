package indiv.abko.taskflow.domain.team.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import indiv.abko.taskflow.domain.team.dto.response.TeamCreateResponse;
import indiv.abko.taskflow.domain.team.entity.Team;
import indiv.abko.taskflow.domain.team.exception.TeamErrorCode;
import indiv.abko.taskflow.domain.team.repository.TeamRepository;
import indiv.abko.taskflow.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CreateTeamUseCase {
	private final TeamRepository teamRepository;

	// 팀 생성
	@Transactional
	public TeamCreateResponse execute(String name, String description) {
		// 400 BAD Request
		if (teamRepository.existsByName(name)) {
			throw new BusinessException(TeamErrorCode.DUPLICATE_TEAM_NAME);
		}

		// 성공 시에는 201 Created
		Team team = new Team(name, description);

		teamRepository.save(team);

		return new TeamCreateResponse(
			team.getId(),
			team.getName(),
			team.getDescription(),
			team.getCreatedAt());
	}
}
