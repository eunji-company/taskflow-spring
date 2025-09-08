package indiv.abko.taskflow.domain.task.service;

import indiv.abko.taskflow.domain.task.dto.reqeust.UpdateTaskRequest;
import indiv.abko.taskflow.domain.task.dto.response.UpdateTaskResponse;
import indiv.abko.taskflow.domain.task.entity.Task;
import indiv.abko.taskflow.domain.task.entity.TaskPriority;
import indiv.abko.taskflow.domain.task.entity.TaskStatus;
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

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

import static indiv.abko.taskflow.domain.task.entity.TaskPriority.MEDIUM;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class UpdateTaskUseCaseTest {

    @Mock
    private TaskRepository taskRepository;
    
    @Mock
    private MemberServiceApi memberServiceApi;
    
    @InjectMocks
    private UpdateTaskUseCase updateTaskUseCase;
    
    @Test
    void Task_수정_성공() {
        //given
        Member member = Member.of(
                "LoginId",
                "1234",
                "gildong@gmail.com",
                "홍길동",
                UserRole.ADMIN
        );
        ReflectionTestUtils.setField(member, "id", 2L);
        
        Task task = new Task(
                "작업 제목",
                "작업 내용",
                LocalDateTime.now().plusDays(7),
                MEDIUM,
                member,
                TaskStatus.DONE
        );
        ReflectionTestUtils.setField(task, "createdAt", LocalDateTime.now());
        ReflectionTestUtils.setField(task, "modifiedAt", LocalDateTime.now());
        ReflectionTestUtils.setField(task, "id", 1L);
        ReflectionTestUtils.setField(task, "member", member);

        UpdateTaskRequest updateTaskRequest = new UpdateTaskRequest();

        LocalDateTime expectedDueDate = LocalDateTime.now().plusDays(10);
        Instant expectedInstant = expectedDueDate.atZone(ZoneId.systemDefault()).toInstant();

        ReflectionTestUtils.setField(updateTaskRequest, "dueDate", expectedDueDate);
        ReflectionTestUtils.setField(updateTaskRequest, "title", "수정한 작업 제목");
        ReflectionTestUtils.setField(updateTaskRequest, "description", "수정한 작업 내용");
        ReflectionTestUtils.setField(updateTaskRequest, "priority", TaskPriority.HIGH);
        ReflectionTestUtils.setField(updateTaskRequest, "assigneeId", 2L);
        
        given(memberServiceApi.getByIdOrThrow(2L)).willReturn(member);
        given(taskRepository.findById(anyLong())).willReturn(Optional.of(task));
        
        //when
        UpdateTaskResponse updateTaskResponse = updateTaskUseCase
                .execute(new AuthMember(1L).memberId(), updateTaskRequest);
        
        //then
        assertThat(updateTaskResponse.title()).isEqualTo("수정한 작업 제목");
        assertThat(updateTaskResponse.description()).isEqualTo("수정한 작업 내용");
        assertThat(updateTaskResponse.dueDate()).isEqualTo(expectedInstant);
        assertThat(updateTaskResponse.priority()).isEqualTo(TaskPriority.HIGH);
        assertThat(updateTaskResponse.assigneeId()).isEqualTo(2L);
    }
}
