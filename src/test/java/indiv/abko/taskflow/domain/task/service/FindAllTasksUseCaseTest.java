package indiv.abko.taskflow.domain.task.service;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.util.ReflectionTestUtils;

import indiv.abko.taskflow.domain.task.dto.response.FindAllTasksResponse;
import indiv.abko.taskflow.domain.task.entity.Task;
import indiv.abko.taskflow.domain.task.entity.TaskPriority;
import indiv.abko.taskflow.domain.task.entity.TaskStatus;
import indiv.abko.taskflow.domain.task.repository.TaskRepository;
import indiv.abko.taskflow.domain.user.entity.Member;
import indiv.abko.taskflow.domain.user.entity.UserRole;
import indiv.abko.taskflow.domain.user.service.MemberServiceApi;
import indiv.abko.taskflow.global.auth.AuthMember;

@ExtendWith(MockitoExtension.class)
public class FindAllTasksUseCaseTest {

	@Mock
	private TaskRepository taskRepository;

	@Mock
	private MemberServiceApi memberServiceApi;

	@InjectMocks
	private FindAllTasksUseCase findAllTasksUseCase;

	@Test
	void Task_전체_조회_성공한다() {
		//given
		AuthMember authMember = new AuthMember(1L);

		Member member = Member.of(
			"LoginId",
			"1234",
			"gildong@gmail.com",
			"홍길동",
			UserRole.ADMIN
		);
		ReflectionTestUtils.setField(member, "id", 1L);

		Task task = new Task(
				"작업 제목",
				"작업 내용",
				LocalDateTime.now().plusDays(7),
				TaskPriority.MEDIUM,
				member,
				TaskStatus.DONE
		);
		ReflectionTestUtils.setField(task, "createdAt", LocalDateTime.now());
		ReflectionTestUtils.setField(task, "modifiedAt", LocalDateTime.now());
		ReflectionTestUtils.setField(task, "member", member);

		Pageable pageable = PageRequest.of(0, 10, Sort.by("createdAt").ascending());
		Page<Task> tasks = new PageImpl<>(List.of(task), pageable, 1);

		given(memberServiceApi.existsById(authMember.memberId())).willReturn(true);
		given(taskRepository.findAllByStatusAndMemberId(TaskStatus.TODO, 1L, pageable))
				.willReturn(tasks);

		//when
		Page<FindAllTasksResponse> findAllTasksResponse = findAllTasksUseCase
			.execute(new AuthMember(1L), TaskStatus.TODO, 1L,0, 10);

		//then
		assertThat(findAllTasksResponse).isNotNull();
	}
}
