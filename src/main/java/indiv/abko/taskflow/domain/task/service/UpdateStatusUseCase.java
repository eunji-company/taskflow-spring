package indiv.abko.taskflow.domain.task.service;

import indiv.abko.taskflow.domain.task.dto.reqeust.UpdateStatusRequest;
import indiv.abko.taskflow.domain.task.dto.response.UpdateStatusResponse;
import indiv.abko.taskflow.domain.task.entity.Task;
import indiv.abko.taskflow.domain.task.exception.TaskErrorCode;
import indiv.abko.taskflow.domain.task.repository.TaskRepository;
import indiv.abko.taskflow.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateStatusUseCase {

    private final TaskRepository taskRepository;

    public UpdateStatusResponse execute(Long taskId, UpdateStatusRequest request) {

        Task task = taskRepository.findById(taskId).orElseThrow(
                () -> new BusinessException(TaskErrorCode.TASK_LIST_EMPTY)
        );

        task.updateStatus(
                request.getStatus()
        );

        return UpdateStatusResponse.fromTask(task);
    }
}
