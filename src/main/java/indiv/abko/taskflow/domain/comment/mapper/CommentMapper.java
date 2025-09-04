package indiv.abko.taskflow.domain.comment.mapper;

import java.time.ZoneOffset;

import org.springframework.stereotype.Component;

import indiv.abko.taskflow.domain.comment.dto.response.WriteCommentToTaskResponse;
import indiv.abko.taskflow.domain.comment.entity.Comment;
import indiv.abko.taskflow.domain.task.entity.Task;
import indiv.abko.taskflow.domain.user.entity.Member;

@Component
public class CommentMapper {
	public WriteCommentToTaskResponse toWriteCommentToTaskResponse(Member member, Task task,
		Comment savedComment) {
		return new WriteCommentToTaskResponse(
			savedComment.getId(),
			savedComment.getContent(),
			task.getId(),
			member.getId(),
			new WriteCommentToTaskResponse.UserResp(
				member.getId(),
				member.getUsername(),
				member.getName(),
				member.getEmail(),
				member.getUserRole().getKey()
			),
			null,
			savedComment.getCreatedAt().toInstant(ZoneOffset.UTC),
			savedComment.getCreatedAt().toInstant(ZoneOffset.UTC)
		);
	}
}
