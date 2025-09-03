package indiv.abko.taskflow.domain.team.entity;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "team_members")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamMember {
	@EmbeddedId
	private TeamMemberId id;
}
