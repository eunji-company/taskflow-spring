package indiv.abko.taskflow.domain.task.dto.response;

import indiv.abko.taskflow.domain.task.entity.Task;
import indiv.abko.taskflow.domain.task.entity.TaskPriority;
import indiv.abko.taskflow.domain.task.entity.TaskStatus;
import indiv.abko.taskflow.domain.user.entity.Member;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CreateTaskResponse(
		Long id,
		String title,
		String description,
		LocalDateTime dueDate,
		TaskPriority priority,
		TaskStatus status,
		Long assigneeId,
		AssigneeResponse assignee,
		LocalDateTime createdAt,
		LocalDateTime updatedAt
){
	public static CreateTaskResponse fromTask(Task task, Member member){
		return CreateTaskResponse.builder()
				.id(task.getId())
				.title(task.getTitle())
				.description(task.getDescription())
				.dueDate(task.getDueDate())
				.priority(task.getPriority())
				.status(task.getStatus())
				.assigneeId(task.getMember().getId())
				.assignee(AssigneeResponse.fromMember(member))
				.createdAt(task.getCreatedAt())
				.updatedAt(task.getModifiedAt())
				.build();
	}
}

