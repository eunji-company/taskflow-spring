package indiv.abko.taskflow.domain.user.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import indiv.abko.taskflow.domain.team.entity.Team;
import indiv.abko.taskflow.domain.user.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
	boolean existsByUsername(String username);

	boolean existsByEmail(String email);

	@Query("""
			select m
			from Member m
			where not exists (
			    select 1
			    from TeamMember tm
			    where tm.id.member = m
			      and tm.id.team   = :team
			)
			order by m.createdAt desc
			""")
	List<Member> findAvailableMembersForTeam(@Param("team") Team team);

	Optional<Member> findByUsername(String username);

}
