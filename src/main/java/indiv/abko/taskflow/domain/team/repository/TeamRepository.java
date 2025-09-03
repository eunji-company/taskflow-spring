package indiv.abko.taskflow.domain.team.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import indiv.abko.taskflow.domain.team.entity.Team;

public interface TeamRepository extends JpaRepository<Team, Long> {
}
