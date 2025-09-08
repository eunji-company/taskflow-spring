package indiv.abko.taskflow.domain.comment.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import indiv.abko.taskflow.domain.comment.dto.command.DeleteMyCommentCommand;
import indiv.abko.taskflow.domain.comment.entity.Comment;
import indiv.abko.taskflow.domain.comment.exception.CommentErrorCode;
import indiv.abko.taskflow.domain.comment.repository.CommentRepository;
import indiv.abko.taskflow.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeleteMyCommentUseCase {
	private final CommentRepository commentRepository;

	@Transactional
	public void execute(DeleteMyCommentCommand command) {
		Comment comment = commentRepository.findWithAuthorById(command.targetCommentId())
			.orElseThrow(() -> new BusinessException(CommentErrorCode.COMMENT_NOT_FOUND));

		if (comment.getMember().getId() != command.requesterId()) {
			throw new BusinessException(CommentErrorCode.COMMENT_FORBIDDEN);
		}

		// 댓글 삭제 시 대댓글 전부 삭제 필요
		commentRepository.deleteAllByParentComment(comment);
		commentRepository.delete(comment);
	}
}
