package indiv.abko.taskflow.domain.task.service;

import indiv.abko.taskflow.domain.task.entity.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import indiv.abko.taskflow.domain.task.dto.response.FindAllTasksResponse;
import indiv.abko.taskflow.domain.task.entity.Task;
import indiv.abko.taskflow.domain.task.exception.TaskErrorCode;
import indiv.abko.taskflow.domain.task.repository.TaskRepository;
import indiv.abko.taskflow.domain.user.service.MemberServiceApi;
import indiv.abko.taskflow.global.auth.AuthMember;
import indiv.abko.taskflow.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FindAllTasksUseCase {

	private final TaskRepository taskRepository;
	private final MemberServiceApi memberServiceApi;

	//Task 전체 조회
	public Page<FindAllTasksResponse> execute(AuthMember authMember, TaskStatus status, Long assigneeId, int page, int size) {

		Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").ascending());

		if (!memberServiceApi.existsById(authMember.memberId())) {
			throw new BusinessException(TaskErrorCode.MEMBER_NOT_FOUND);
		}

		Page<Task> tasks;

		if (status != null && assigneeId != null) {
			tasks = taskRepository.findAllByStatusAndMemberId(status, assigneeId, pageable);
		} else if (status != null) {
			tasks = taskRepository.findAllByStatus(status, pageable);
		} else if (assigneeId != null) {
			tasks = taskRepository.findAllByMemberId(assigneeId, pageable);
		} else {
			tasks = taskRepository.findAll(pageable);
		}

		return tasks.map(FindAllTasksResponse::fromTask);
	}
}
