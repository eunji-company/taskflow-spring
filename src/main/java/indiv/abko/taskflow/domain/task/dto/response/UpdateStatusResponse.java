package indiv.abko.taskflow.domain.task.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import indiv.abko.taskflow.domain.task.entity.Task;
import indiv.abko.taskflow.domain.task.entity.TaskPriority;
import indiv.abko.taskflow.domain.task.entity.TaskStatus;
import indiv.abko.taskflow.domain.user.entity.Member;
import indiv.abko.taskflow.global.dto.DtoConstants;
import lombok.Builder;

import java.time.Instant;
import java.time.ZoneId;

@Builder
public record UpdateStatusResponse (
        Long id,
        String title,
        String description,
        TaskStatus status,
        TaskPriority priority,
        Long assigneeId,
        AssigneeResponse assignee,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DtoConstants.TIME_FORMAT, timezone = DtoConstants.DISPLAY_TIME_ZONE_STRING)
        Instant createdAt,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DtoConstants.TIME_FORMAT, timezone = DtoConstants.DISPLAY_TIME_ZONE_STRING)
        Instant updatedAt,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DtoConstants.TIME_FORMAT, timezone = DtoConstants.DISPLAY_TIME_ZONE_STRING)
        Instant dueDate

) {
    public static UpdateStatusResponse fromTask(Task task) {

        Member assignee = task.getMember();

        return UpdateStatusResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .priority(task.getPriority())
                .assigneeId(task.getMember().getId())
                .assignee(AssigneeResponse.fromMember(assignee))
                .createdAt(task.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant())
                .updatedAt(task.getModifiedAt().atZone(ZoneId.systemDefault()).toInstant())
                .dueDate(task.getDueDate().atZone(ZoneId.systemDefault()).toInstant())
                .build();
    }
}
