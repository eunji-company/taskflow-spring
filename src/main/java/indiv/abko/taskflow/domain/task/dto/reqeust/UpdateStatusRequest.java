package indiv.abko.taskflow.domain.task.dto.reqeust;

import indiv.abko.taskflow.domain.task.entity.TaskStatus;
import lombok.Getter;

@Getter
public class UpdateStatusRequest {

    private TaskStatus status;
}
