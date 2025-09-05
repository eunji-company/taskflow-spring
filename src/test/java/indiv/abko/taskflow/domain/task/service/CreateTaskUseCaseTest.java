package indiv.abko.taskflow.domain.task.service;

import indiv.abko.taskflow.domain.task.dto.reqeust.CreateTaskRequest;
import indiv.abko.taskflow.domain.task.dto.response.CreateTaskResponse;
import indiv.abko.taskflow.domain.task.entity.Task;
import indiv.abko.taskflow.domain.task.entity.TaskPriority;
import indiv.abko.taskflow.domain.task.repository.TaskRepository;
import indiv.abko.taskflow.domain.user.entity.Member;
import indiv.abko.taskflow.domain.user.entity.UserRole;
import indiv.abko.taskflow.domain.user.service.MemberServiceApi;
import indiv.abko.taskflow.global.auth.AuthMember;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class CreateTaskUseCaseTest {

	@Mock
	private TaskRepository taskRepository;

	@Mock
	private MemberServiceApi memberServiceApi;

	@InjectMocks
	private CreateTaskUseCase createTaskUseCase;

	@Test
	void Task_생성_성공() {
		// given
		Member member = Member.of(
			"LoginId",
			"1234",
			"gildong@gmail.com",
			"홍길동",
			UserRole.ADMIN);

		ReflectionTestUtils.setField(member, "id", 1L);

		Task task = new Task(
			"작업 제목",
			"작업 내용",
			LocalDateTime.now().plusDays(7),
			TaskPriority.MEDIUM,
			1L);

		ReflectionTestUtils.setField(task, "member", member);

		CreateTaskRequest createTaskRequest = new CreateTaskRequest();

		ReflectionTestUtils.setField(createTaskRequest, "title", "작업 제목");
		ReflectionTestUtils.setField(createTaskRequest, "description", "작업 내용");
		ReflectionTestUtils.setField(createTaskRequest, "dueDate", LocalDateTime.now().plusDays(7));
		ReflectionTestUtils.setField(createTaskRequest, "priority", TaskPriority.MEDIUM);
		ReflectionTestUtils.setField(createTaskRequest, "assigneeId", 1L);

		given(memberServiceApi.getByIdOrThrow(anyLong())).willReturn(member);
		given(taskRepository.save(any())).willReturn(task);

		// when
		CreateTaskResponse createTaskResponse = createTaskUseCase
			.execute(new AuthMember(1L), createTaskRequest);

		//then
		assertThat(createTaskResponse).isNotNull();
		assertThat(createTaskResponse.title()).isEqualTo("작업 제목");

	}

}
