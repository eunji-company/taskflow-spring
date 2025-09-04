package indiv.abko.taskflow.domain.team.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import indiv.abko.taskflow.domain.team.dto.response.UpdateTeamResponse;
import indiv.abko.taskflow.domain.team.entity.Team;
import indiv.abko.taskflow.domain.team.exception.TeamErrorCode;
import indiv.abko.taskflow.domain.team.repository.TeamRepository;
import indiv.abko.taskflow.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UpdateTeamUseCase {
	private final TeamRepository teamRepository;

	// 팀 수정
	@Transactional
	public UpdateTeamResponse execute(String name, String description, Long teamId) {
		Team team = teamRepository.findById(teamId).orElseThrow(
			() -> new BusinessException(TeamErrorCode.NOT_FOUND_TEAM)
		);

		team.update(name, description);

		return new UpdateTeamResponse(
			team.getId(),
			team.getName(),
			team.getDescription(),
			team.getCreatedAt()
		);
	}
}
