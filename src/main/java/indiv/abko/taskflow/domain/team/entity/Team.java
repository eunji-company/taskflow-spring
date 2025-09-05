package indiv.abko.taskflow.domain.team.entity;

import java.util.ArrayList;
import java.util.List;

import indiv.abko.taskflow.domain.user.entity.Member;
import indiv.abko.taskflow.global.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "teams")
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Team extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true, nullable = false)
	private String name;

	@Column(nullable = false)
	private String description;

	// 주인이 아닌 쪽은 읽기밖에 안됨
	@OneToMany(mappedBy = "id.team")
	private List<TeamMember> teamMembers = new ArrayList<>();

	public Team(String name, String description) {
		this.name = name;
		this.description = description;
	}

	// 헬퍼 메서드
	public void addMember(Member member) {
		TeamMemberId id = new TeamMemberId(this, member);
		TeamMember teamMember = new TeamMember(id);
		this.teamMembers.add(teamMember);
	}

	public void update(String name, String description) {
		this.name = name;
		this.description = description;
	}
}
