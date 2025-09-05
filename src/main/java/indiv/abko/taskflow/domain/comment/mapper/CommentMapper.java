package indiv.abko.taskflow.domain.comment.mapper;

import org.springframework.stereotype.Component;

import indiv.abko.taskflow.domain.comment.dto.command.DeleteMyCommentCommand;
import indiv.abko.taskflow.domain.comment.dto.command.WriteCommentToCommentCommand;
import indiv.abko.taskflow.domain.comment.dto.command.WriteCommentToTaskCommand;
import indiv.abko.taskflow.domain.comment.dto.request.WriteCommentRequest;
import indiv.abko.taskflow.domain.comment.dto.response.WriteCommentToCommentResponse;
import indiv.abko.taskflow.domain.comment.dto.response.WriteCommentToTaskResponse;
import indiv.abko.taskflow.domain.comment.entity.Comment;
import indiv.abko.taskflow.domain.task.entity.Task;
import indiv.abko.taskflow.domain.user.entity.Member;
import indiv.abko.taskflow.global.auth.AuthMember;
import indiv.abko.taskflow.global.dto.DtoConstants;

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
				member.getUserRole().name()),
			null,
			savedComment.getCreatedAt().toInstant(DtoConstants.REAL_TIME_ZONE_OFFSET),
			savedComment.getCreatedAt().toInstant(DtoConstants.REAL_TIME_ZONE_OFFSET));
	}

	public WriteCommentToTaskCommand toWriteCommentToTaskCommand(AuthMember authMember, long taskId,
		WriteCommentRequest request) {
		return new WriteCommentToTaskCommand(authMember.memberId(), taskId, request.content());
	}

	public DeleteMyCommentCommand toDeleteMyCommentCommand(AuthMember member, long commentId) {
		return new DeleteMyCommentCommand(member.memberId(), commentId);
	}

	public WriteCommentToCommentResponse toWriteCommentToCommentResponse(Member author, long taskId,
		long parentCommentId,
		Comment savedComment) {
		return WriteCommentToCommentResponse.builder()
			.id(savedComment.getId())
			.content(savedComment.getContent())
			.createdAt(savedComment.getCreatedAt().toInstant(DtoConstants.REAL_TIME_ZONE_OFFSET))
			.parentId(parentCommentId)
			.updatedAt(savedComment.getCreatedAt().toInstant(DtoConstants.REAL_TIME_ZONE_OFFSET))
			.user(new WriteCommentToCommentResponse.UserResp(
				author.getId(),
				author.getUsername(),
				author.getName(),
				author.getEmail(),
				author.getUserRole().name()))
			.userId(author.getId())
			.build();
	}

	public WriteCommentToCommentCommand toWriteCommentToCommentCommand(AuthMember authMember, long taskId,
		WriteCommentRequest request) {
		return new WriteCommentToCommentCommand(authMember.memberId(), taskId, request.parentId(), request.content());
	}
}
