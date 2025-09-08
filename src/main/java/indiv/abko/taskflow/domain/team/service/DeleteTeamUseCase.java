package indiv.abko.taskflow.domain.team.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import indiv.abko.taskflow.domain.team.entity.Team;
import indiv.abko.taskflow.domain.team.exception.TeamErrorCode;
import indiv.abko.taskflow.domain.team.repository.TeamRepository;
import indiv.abko.taskflow.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeleteTeamUseCase {
	private final TeamRepository teamRepository;

	// 팀 삭제
	@Transactional
	public void execute(Long teamId) {
		Team team = teamRepository.findById(teamId).orElseThrow(
			() -> new BusinessException(TeamErrorCode.NOT_FOUND_TEAM)
		);

		teamRepository.delete(team);
	}
}
