package indiv.abko.taskflow.domain.task.dto.reqeust;

import indiv.abko.taskflow.domain.task.entity.TaskPriority;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UpdateTaskRequest {

    private String title;

    private String description;

    private LocalDateTime dueDate;

    private TaskPriority priority;

    private Long assigneeId;

}
