package indiv.abko.taskflow.domain.task.service;

import indiv.abko.taskflow.domain.task.dto.response.FindTaskResponse;
import indiv.abko.taskflow.domain.task.entity.Task;
import indiv.abko.taskflow.domain.task.exception.TaskErrorCode;
import indiv.abko.taskflow.domain.task.repository.TaskRepository;
import indiv.abko.taskflow.domain.user.service.MemberServiceApi;
import indiv.abko.taskflow.global.auth.AuthMember;
import indiv.abko.taskflow.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FindTaskUseCase {

    private final TaskRepository taskRepository;

    //Task 상세 조회
    @Transactional(readOnly = true)
    public FindTaskResponse execute(Long taskId){

        Task task = taskRepository.findById(taskId).orElseThrow(
                () -> new BusinessException(TaskErrorCode.TASK_NOT_FOUND)
        );
        return FindTaskResponse.fromTask(task);
    }

}
