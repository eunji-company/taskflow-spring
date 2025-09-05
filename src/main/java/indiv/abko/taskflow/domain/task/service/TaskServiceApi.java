package indiv.abko.taskflow.domain.task.service;

import indiv.abko.taskflow.domain.task.entity.Task;

public interface TaskServiceApi {

	Task getByIdOrThrow(long taskId);
}
