package indiv.abko.taskflow.domain.task.controller;

import indiv.abko.taskflow.domain.task.dto.reqeust.CreateTaskRequest;
import indiv.abko.taskflow.domain.task.dto.reqeust.UpdateTaskRequest;
import indiv.abko.taskflow.domain.task.dto.response.*;
import indiv.abko.taskflow.domain.task.entity.Task;
import indiv.abko.taskflow.domain.task.service.CreateTaskUseCase;
import indiv.abko.taskflow.domain.task.service.FindAllTasksUseCase;
import indiv.abko.taskflow.domain.task.service.FindTaskUseCase;
import indiv.abko.taskflow.domain.task.service.UpdateTaskUseCase;
import indiv.abko.taskflow.global.auth.AuthMember;
import indiv.abko.taskflow.global.dto.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tasks")
public class TaskController {

	private final CreateTaskUseCase createTaskUseCase;
	private final FindAllTasksUseCase findAllTasksUseCase;
	private final FindTaskUseCase findTaskUseCase;
	private final UpdateTaskUseCase updateTaskUseCase;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public CommonResponse<CreateTaskResponse> createTask(
			@AuthenticationPrincipal AuthMember authMember,
			@RequestBody CreateTaskRequest request
	) {
		CreateTaskResponse response = createTaskUseCase.execute(authMember, request);
		return CommonResponse.success("Task가 생성되었습니다.", response);
	}

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public CommonResponse<PageResponse<FindAllTasksResponse>> findAllTasks(
			@AuthenticationPrincipal AuthMember authMember,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size
	) {
		Page<FindAllTasksResponse> tasks = findAllTasksUseCase.execute(authMember, page, size);
		PageResponse<FindAllTasksResponse> response = new PageResponse<>(tasks);
		return CommonResponse.success("Task 목록을 조회했습니다.", response);
	}

	@GetMapping("/{taskId}")
	@ResponseStatus(HttpStatus.OK)
	public CommonResponse<FindTaskResponse> findTask(
			@PathVariable Long taskId
	) {
		FindTaskResponse response = findTaskUseCase.execute(taskId);
        return CommonResponse.success("Task를 조회했습니다.", response);
    }

	@PatchMapping("/{taskId}")
	@ResponseStatus(HttpStatus.OK)
	public CommonResponse<UpdateTaskResponse> updateTask(
		@AuthenticationPrincipal AuthMember authMember,
		@PathVariable Long taskId,
		@RequestBody UpdateTaskRequest request
	) {
		UpdateTaskResponse response = updateTaskUseCase.execute(authMember, taskId, request);
		return CommonResponse.success("Task가 수정되었습니다.", response);
	}
}
