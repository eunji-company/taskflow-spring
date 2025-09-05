package indiv.abko.taskflow.domain.team.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import indiv.abko.taskflow.domain.team.entity.TeamMember;
import indiv.abko.taskflow.domain.team.entity.TeamMemberId;

public interface TeamMemberRepository extends JpaRepository<TeamMember, TeamMemberId> {
	@EntityGraph(attributePaths = {"id.team", "id.member"})
	List<TeamMember> findAllWithFetchBy();
}
