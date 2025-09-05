package indiv.abko.taskflow.domain.team.entity;

import java.io.Serializable;

import indiv.abko.taskflow.domain.user.entity.Member;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@EqualsAndHashCode
public class TeamMemberId implements Serializable {
	@ManyToOne(optional = false)
	@JoinColumn(nullable = false)
	private Team team;

	@ManyToOne(optional = false)
	@JoinColumn(nullable = false)
	private Member member;
}
