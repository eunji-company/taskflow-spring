package indiv.abko.taskflow.domain.task.service;

import indiv.abko.taskflow.domain.task.entity.Task;
import indiv.abko.taskflow.domain.task.exception.TaskErrorCode;
import indiv.abko.taskflow.domain.task.repository.TaskRepository;
import indiv.abko.taskflow.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskService implements TaskServiceApi {

	private final TaskRepository taskRepository;

	public Task getByIdOrThrow(long taskId) {

		return taskRepository.findById(taskId).orElseThrow(
				() -> new BusinessException(TaskErrorCode.TASK_NOT_FOUND)
		);
	}
}
