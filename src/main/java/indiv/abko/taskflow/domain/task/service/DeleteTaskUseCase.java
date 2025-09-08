package indiv.abko.taskflow.domain.task.service;

import indiv.abko.taskflow.domain.task.entity.Task;
import indiv.abko.taskflow.domain.task.exception.TaskErrorCode;
import indiv.abko.taskflow.domain.task.repository.TaskRepository;
import indiv.abko.taskflow.global.auth.AuthMember;
import indiv.abko.taskflow.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeleteTaskUseCase {

    private final TaskRepository taskRepository;

    //Task 삭제
    @Transactional
    public void execute(Long taskId) {

        Task task = taskRepository.findById(taskId).orElseThrow(
                () -> new BusinessException(TaskErrorCode.TASK_NOT_FOUND)
        );

        taskRepository.delete(task);
    }
}
