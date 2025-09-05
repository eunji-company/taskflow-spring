package indiv.abko.taskflow.domain.team.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import indiv.abko.taskflow.domain.team.entity.Team;

public interface TeamRepository extends JpaRepository<Team, Long> {
	boolean existsByName(String name);

	@EntityGraph(attributePaths = {"teamMembers", "teamMembers.id.member"})
	Optional<Team> findWithTeamMembersById(Long id);
}
