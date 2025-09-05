package indiv.abko.taskflow.domain.comment.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import indiv.abko.taskflow.domain.comment.dto.command.DeleteMyCommentCommand;
import indiv.abko.taskflow.domain.comment.dto.command.WriteCommentToCommentCommand;
import indiv.abko.taskflow.domain.comment.dto.command.WriteCommentToTaskCommand;
import indiv.abko.taskflow.domain.comment.dto.request.WriteCommentRequest;
import indiv.abko.taskflow.domain.comment.dto.response.WriteCommentToCommentResponse;
import indiv.abko.taskflow.domain.comment.dto.response.WriteCommentToTaskResponse;
import indiv.abko.taskflow.domain.comment.mapper.CommentMapper;
import indiv.abko.taskflow.domain.comment.service.DeleteMyCommentUseCase;
import indiv.abko.taskflow.domain.comment.service.WriteCommentToCommentUseCase;
import indiv.abko.taskflow.domain.comment.service.WriteCommentToTaskUseCase;
import indiv.abko.taskflow.global.auth.AuthMember;
import indiv.abko.taskflow.global.dto.CommonResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentController {
	private final WriteCommentToTaskUseCase writeCommentToTaskUseCase;
	private final WriteCommentToCommentUseCase writeCommentToCommentUseCase;
	private final DeleteMyCommentUseCase deleteMyCommentUseCase;
	private final CommentMapper commentMapper;

	@PostMapping("/tasks/{taskId}/comments")
	@ResponseStatus(HttpStatus.CREATED)
	public CommonResponse<?> writeComment(@AuthenticationPrincipal
	AuthMember authMember,
		@PathVariable("taskId")
		long taskId,
		@Valid @RequestBody
		WriteCommentRequest request) {
		if (request.parentId() == null) {
			WriteCommentToTaskCommand command = commentMapper.toWriteCommentToTaskCommand(authMember, taskId, request);
			WriteCommentToTaskResponse result = writeCommentToTaskUseCase.execute(command);
			return CommonResponse.success("댓글이 생성되었습니다.", result);
		} else {
			WriteCommentToCommentCommand command = commentMapper.toWriteCommentToCommentCommand(authMember, taskId,
				request);
			WriteCommentToCommentResponse resp = writeCommentToCommentUseCase.execute(command);
			return CommonResponse.success("대댓글이 생성되었습니다.", resp);
		}
	}

	@DeleteMapping("/tasks/{taskId}/comments/{commentId}")
	public CommonResponse<Void> deleteMyComment(@AuthenticationPrincipal
	AuthMember member,
		@PathVariable("taskId")
		long taskId,
		@PathVariable("commentId")
		long commentId) {
		DeleteMyCommentCommand command = commentMapper.toDeleteMyCommentCommand(member, commentId);
		deleteMyCommentUseCase.execute(command);
		return CommonResponse.success("댓글이 삭제되었습니다.", null);
	}

}
