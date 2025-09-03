package indiv.abko.taskflow.domain.comment.service;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import indiv.abko.taskflow.domain.comment.dto.command.WriteCommentToTaskCommand;
import indiv.abko.taskflow.domain.comment.dto.response.WriteCommentToTaskResponse;
import indiv.abko.taskflow.domain.comment.entity.Comment;
import indiv.abko.taskflow.domain.comment.mapper.CommentMapper;
import indiv.abko.taskflow.domain.comment.repository.CommentRepository;
import indiv.abko.taskflow.domain.task.entity.Task;
import indiv.abko.taskflow.domain.task.service.TaskServiceApi;
import indiv.abko.taskflow.domain.user.entity.Member;
import indiv.abko.taskflow.domain.user.service.MemberServiceApi;

@ExtendWith(MockitoExtension.class)
public class WriteCommentToTaskUseCaseTest {
	@Mock
	private CommentRepository commentRepository;

	@Mock
	private MemberServiceApi memberService;

	@Mock
	private TaskServiceApi taskService;

	@Mock
	private CommentMapper commentMapper;

	@InjectMocks
	private WriteCommentToTaskUseCase writeCommentToTaskUseCase;

	@Test
	void 성공적으로_comment를_task에_등록한다() {
		// given
		long testMemberId = 1L;
		long testTaskId = 1L;
		String testContent = "테스트";
		Member requester = Mockito.mock(Member.class);
		Task task = Mockito.mock(Task.class);
		String content = "content";
		WriteCommentToTaskResponse resp = new WriteCommentToTaskResponse(
			1L,
			null,
			testTaskId,
			testMemberId,
			null,
			null,
			null,
			null
		);
		WriteCommentToTaskCommand command = new WriteCommentToTaskCommand(testMemberId, testTaskId,
			testContent);

		given(memberService.getByIdOrThrow(testMemberId)).willReturn(requester);
		given(taskService.getByIdOrThrow(testTaskId)).willReturn(task);
		given(commentMapper.toWriteCommentToTaskResponse(any(), any(), any())).willReturn(resp);

		// when
		writeCommentToTaskUseCase.execute(command);

		// then
		then(commentRepository).should(times(1)).save(any(Comment.class));
	}
}
