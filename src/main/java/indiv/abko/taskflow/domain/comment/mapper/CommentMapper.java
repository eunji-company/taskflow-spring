package indiv.abko.taskflow.domain.comment.mapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import indiv.abko.taskflow.domain.comment.dto.command.DeleteMyCommentCommand;
import indiv.abko.taskflow.domain.comment.dto.command.ViewCommentsFromTaskCommand;
import indiv.abko.taskflow.domain.comment.dto.command.WriteCommentToCommentCommand;
import indiv.abko.taskflow.domain.comment.dto.command.WriteCommentToTaskCommand;
import indiv.abko.taskflow.domain.comment.dto.request.WriteCommentRequest;
import indiv.abko.taskflow.domain.comment.dto.response.ViewCommentsFromTaskResponse;
import indiv.abko.taskflow.domain.comment.dto.response.ViewCommentsFromTaskResponse.CommentResp;
import indiv.abko.taskflow.domain.comment.dto.response.WriteCommentToCommentResponse;
import indiv.abko.taskflow.domain.comment.dto.response.WriteCommentToTaskResponse;
import indiv.abko.taskflow.domain.comment.entity.Comment;
import indiv.abko.taskflow.domain.comment.enums.CommentSortOption;
import indiv.abko.taskflow.domain.comment.util.HierarchicalCommentsPaginator.PagedComments;
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

	public ViewCommentsFromTaskCommand toViewCommentsFromTaskCommand(Pageable pageable, long taskId, String sort) {
		CommentSortOption sortOption = "newest".equalsIgnoreCase(sort)
			? CommentSortOption.NEWEST
			: CommentSortOption.OLDEST;
		return new ViewCommentsFromTaskCommand(pageable, taskId, sortOption);
	}

	public ViewCommentsFromTaskResponse toViewCommentsFromTaskResponse(PagedComments results) {
		List<ViewCommentsFromTaskResponse.CommentResp> commentResps = new ArrayList<>();

		for (Comment comment : results.comments()) {
			Member author = comment.getMember();
			Long parentCommentId = comment.getParentComment() != null
				? comment.getParentComment().getId() : null;

			ViewCommentsFromTaskResponse.UserResp userResp = ViewCommentsFromTaskResponse.UserResp.builder()
				.id(author.getId())
				.username(author.getUsername())
				.name(author.getName())
				.email(author.getEmail())
				.role(author.getUserRole().name())
				.build();

			CommentResp commentResp = ViewCommentsFromTaskResponse.CommentResp.builder()
				.id(comment.getId())
				.content(comment.getContent())
				.taskId(comment.getTask().getId())
				.userId(author.getId())
				.user(userResp)
				.createdAt(comment.getCreatedAt().toInstant(DtoConstants.REAL_TIME_ZONE_OFFSET))
				.updatedAt(comment.getCreatedAt().toInstant(DtoConstants.REAL_TIME_ZONE_OFFSET))
				.parentId(parentCommentId)
				.build();
			commentResps.add(commentResp);
		}

		return ViewCommentsFromTaskResponse.builder()
			.content(commentResps)
			.totalElements(results.totalElements())
			.totalPages(results.totalPages())
			.size(results.size())
			.number(results.number())
			.build();
	}
}
