package indiv.abko.taskflow.domain.team.service;

import java.util.ArrayList;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import indiv.abko.taskflow.domain.team.dto.response.CreateTeamResponse;
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
	public CreateTeamResponse execute(String name, String description) {
		if (teamRepository.existsByName(name)) {
			throw new BusinessException(TeamErrorCode.DUPLICATE_TEAM_NAME);
		}

		Team team = new Team(name, description);

		teamRepository.save(team);

		return new CreateTeamResponse(
			team.getId(),
			team.getName(),
			team.getDescription(),
			team.getCreatedAt(),
			new ArrayList<>());
	}
}
