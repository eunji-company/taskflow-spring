package indiv.abko.taskflow.domain.task.dto.response;

import indiv.abko.taskflow.domain.user.entity.Member;
import lombok.Builder;
import lombok.Getter;

@Builder
public record AssigneeResponse(Long id,
							   String username,
							   String name,
							   String email
) {
	public static AssigneeResponse fromMember(Member member){
		return AssigneeResponse.builder()
				.id(member.getId())
				.username(member.getUsername())
				.name(member.getName())
				.email(member.getEmail())
				.build();
	}

}