package indiv.abko.taskflow.domain.task.controller;

import indiv.abko.taskflow.domain.task.dto.reqeust.CreateTaskRequest;
import indiv.abko.taskflow.domain.task.dto.response.CreateTaskResponse;
import indiv.abko.taskflow.domain.task.service.CreateTaskUseCase;
import indiv.abko.taskflow.global.auth.AuthMember;
import indiv.abko.taskflow.global.dto.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tasks")
public class TaskController {

	private final CreateTaskUseCase createTaskUseCase;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public CommonResponse<CreateTaskResponse> createTask(
		@AuthenticationPrincipal
		AuthMember authMember,
		@RequestBody
		CreateTaskRequest request) {
		CreateTaskResponse response = createTaskUseCase.execute(authMember, request);
		return CommonResponse.success("Task가 생성되었습니다.", response);
	}

	//	@GetMapping
	//	@ResponseStatus(HttpStatus.OK)
	//	public CommonResponse<Page<TaskListResponse>> taskList(
	//			@AuthenticationPrincipal AuthMember authMember,
	//			@RequestParam(defaultValue = "0") int page,
	//			@RequestParam(defaultValue = "10") int size
	//	){
	//		Page<TaskListResponse> response = taskListUseCase.taskList(page, size);
	//		return CommonResponse.success("Task 목록을 조회했습니다.", response);
	//	}

}
