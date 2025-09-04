package indiv.abko.taskflow.domain.comment.mapper;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import indiv.abko.taskflow.domain.comment.dto.response.WriteCommentToTaskResponse;
import indiv.abko.taskflow.domain.comment.entity.Comment;
import indiv.abko.taskflow.domain.task.entity.Task;
import indiv.abko.taskflow.domain.user.entity.Member;
import indiv.abko.taskflow.domain.user.entity.UserRole;

@ExtendWith(MockitoExtension.class)
public class CommentMapperTest {
	private final CommentMapper commentMapper = new CommentMapper();

	@Test
	void WriteCommentToTaskResponse를_정확하게_매핑한다() {
		// given
		Member requester = Mockito.mock(Member.class);
		Task task = Mockito.mock(Task.class);
		Comment savedComment = Mockito.mock(Comment.class);

		long requesterId = 1L;
		String requesterUsername = "requester";
		String requesterEmail = "email";
		String requesterName = "name";
		UserRole requesterRole = UserRole.USER;

		long taskId = 1L;

		long commentId = 1L;
		String commentContent = "content";
		LocalDateTime now = LocalDateTime.now();

		given(requester.getId()).willReturn(requesterId);
		given(requester.getUsername()).willReturn(requesterUsername);
		given(requester.getEmail()).willReturn(requesterEmail);
		given(requester.getName()).willReturn(requesterName);
		given(requester.getUserRole()).willReturn(requesterRole);

		given(task.getId()).willReturn(taskId);

		given(savedComment.getId()).willReturn(commentId);
		given(savedComment.getContent()).willReturn(commentContent);
		given(savedComment.getCreatedAt()).willReturn(now);

		// when
		WriteCommentToTaskResponse resp = commentMapper.toWriteCommentToTaskResponse(requester,
			task, savedComment);

		// then
		assertNotNull(resp);
		assertEquals(requesterId, resp.userId());
		assertEquals(requesterName, resp.user().name());
		assertEquals(commentId, resp.id());
		assertEquals(commentContent, resp.content());
		assertEquals(taskId, resp.taskId());
	}
}