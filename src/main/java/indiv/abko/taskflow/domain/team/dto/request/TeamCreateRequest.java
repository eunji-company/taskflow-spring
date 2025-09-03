package indiv.abko.taskflow.domain.team.dto.request;

import lombok.Getter;

@Getter
public class TeamCreateRequest {
	private String name;                // 이름
	private String description;        // 설명
}
