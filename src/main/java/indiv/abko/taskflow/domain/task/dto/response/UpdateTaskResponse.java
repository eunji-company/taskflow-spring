package indiv.abko.taskflow.domain.task.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import indiv.abko.taskflow.domain.task.entity.TaskPriority;
import indiv.abko.taskflow.global.dto.DtoConstants;
import lombok.Getter;

import java.time.Instant;
import java.time.LocalDateTime;

public record UpdateTaskResponse(
        String title,
        String description,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DtoConstants.TIME_FORMAT, timezone = DtoConstants.DISPLAY_TIME_ZONE_STRING)
        Instant dueDate,
        TaskPriority priority,
        Long assigneeId
) {

}
