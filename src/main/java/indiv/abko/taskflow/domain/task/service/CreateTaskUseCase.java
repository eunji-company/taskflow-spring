package indiv.abko.taskflow.domain.task.service;

import indiv.abko.taskflow.domain.task.dto.reqeust.CreateTaskRequest;
import indiv.abko.taskflow.domain.task.dto.response.CreateTaskResponse;
import indiv.abko.taskflow.domain.task.entity.Task;
import indiv.abko.taskflow.domain.task.repository.TaskRepository;
import indiv.abko.taskflow.domain.user.service.MemberServiceApi;
import indiv.abko.taskflow.global.auth.AuthMember;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateTaskUseCase {

	private final MemberServiceApi memberServiceApi;
	private final TaskRepository taskRepository;

	//Task 생성
	@Transactional
	public CreateTaskResponse execute(AuthMember authMember, CreateTaskRequest request) {

//		Member member = memberServiceApi.getByIdOrThrow(request.getAssigneeId()); 사용 안 하는것 같아서 일단 주석처리

		Task task = new Task(
				request.getTitle(),
				request.getDescription(),
				request.getDueDate(),
				request.getPriority(),
				request.getAssigneeId()
		);

		task = taskRepository.save(task);

		return CreateTaskResponse.fromTask(task);
	}
}
