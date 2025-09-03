package indiv.abko.taskflow.domain.user.dto;

import java.time.LocalDateTime;

import indiv.abko.taskflow.domain.user.entity.Member;
import lombok.Getter;

@Getter
public class MemberInfoResponse {
	private Long id;
	private String username;
	private String email;
	private String name;
	private String role;
	private LocalDateTime createdAt;

	public MemberInfoResponse(Member member) {
		this.id = member.getId();
		this.username = member.getUsername();
		this.email = member.getEmail();
		this.name = member.getName();
		this.role = member.getUserRole().name();
		this.createdAt = member.getCreatedAt();
	}

	public static MemberInfoResponse from(Member member) {
		return new MemberInfoResponse(member);
	}
}
