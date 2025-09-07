package indiv.abko.taskflow.domain.task.service;

import indiv.abko.taskflow.domain.task.dto.reqeust.UpdateTaskRequest;
import indiv.abko.taskflow.domain.task.dto.response.UpdateTaskResponse;
import indiv.abko.taskflow.domain.task.entity.Task;
import indiv.abko.taskflow.domain.task.exception.TaskErrorCode;
import indiv.abko.taskflow.domain.task.repository.TaskRepository;
import indiv.abko.taskflow.domain.user.entity.Member;
import indiv.abko.taskflow.domain.user.repository.MemberRepository;
import indiv.abko.taskflow.domain.user.service.MemberService;
import indiv.abko.taskflow.domain.user.service.MemberServiceApi;
import indiv.abko.taskflow.global.auth.AuthMember;
import indiv.abko.taskflow.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;

@Service
@RequiredArgsConstructor
public class UpdateTaskUseCase {

    private final TaskRepository taskRepository;
    private final MemberServiceApi memberServiceApi;

    //Task 수정
    @Transactional
    public UpdateTaskResponse execute(AuthMember authMember, Long taskId, UpdateTaskRequest request){

        Member member = memberServiceApi.getByIdOrThrow(request.getAssigneeId());

        Task task = taskRepository.findById(taskId).orElseThrow(
                () -> new BusinessException(TaskErrorCode.TASK_LIST_EMPTY)
        );

        if (authMember == null) {
            throw new BusinessException(TaskErrorCode.TASK_UNAUTHORIZED); // 인증 안 된 경우
        }

        task.updateTask(
                request.getTitle(),
                request.getDescription(),
                request.getDueDate(),
                request.getPriority(),
                request.getAssigneeId()
        );
        return new UpdateTaskResponse(
                task.getTitle(),
                task.getDescription(),
                task.getDueDate().atZone(ZoneId.systemDefault()).toInstant(),
                task.getPriority(),
                task.getAssigneeId()
        );
    }
}
