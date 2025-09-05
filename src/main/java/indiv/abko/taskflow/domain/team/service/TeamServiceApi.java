package indiv.abko.taskflow.domain.team.service;

import indiv.abko.taskflow.domain.team.entity.Team;
import indiv.abko.taskflow.domain.user.entity.Member;

public interface TeamServiceApi {
	Team getByIdOrThrow(Long teamId);

	void assertMemberInTeamOrThrow(Team team, Member member);
}
