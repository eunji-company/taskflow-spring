package indiv.abko.taskflow.domain.comment.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import indiv.abko.taskflow.domain.comment.dto.command.DeleteMyCommentCommand;
import indiv.abko.taskflow.domain.comment.entity.Comment;
import indiv.abko.taskflow.domain.comment.exception.CommentErrorCode;
import indiv.abko.taskflow.domain.comment.repository.CommentRepository;
import indiv.abko.taskflow.domain.user.entity.Member;
import indiv.abko.taskflow.global.exception.BusinessException;

@ExtendWith(MockitoExtension.class)
public class DeleteMyCommentUseCaseTest {
	@Mock
	private CommentRepository commentRepository;

	@InjectMocks
	private DeleteMyCommentUseCase deleteMyCommentUseCase;

	@Test
	void 나의_댓글을_삭제한다() {
		// given
		Member member = Mockito.mock(Member.class);
		given(member.getId()).willReturn(1L);
		Comment comment = Mockito.mock(Comment.class);
		given(comment.getMember()).willReturn(member);

		DeleteMyCommentCommand command = new DeleteMyCommentCommand(1L, 1L);

		given(commentRepository.findWithAuthorById(1L)).willReturn(Optional.of(comment));
		willDoNothing().given(commentRepository).deleteAllByParentComment(comment);
		willDoNothing().given(commentRepository).delete(comment);

		// when & then
		assertDoesNotThrow(() -> deleteMyCommentUseCase.execute(command));
		then(commentRepository).should(times(1)).deleteAllByParentComment(comment);
		then(commentRepository).should(times(1)).delete(comment);
	}

	@Test
	void 댓글이_존재하지_않으면_실패한다() {
		// given
		DeleteMyCommentCommand command = new DeleteMyCommentCommand(1L, 1L);

		Member member = Mockito.mock(Member.class);
		given(member.getId()).willReturn(1L);
		Comment comment = Mockito.mock(Comment.class);
		given(comment.getMember()).willReturn(member);

		given(commentRepository.findWithAuthorById(1L)).willReturn(Optional.empty());

		// when & then
		BusinessException ex = assertThrows(BusinessException.class, () -> deleteMyCommentUseCase.execute(command));
		assertEquals(CommentErrorCode.COMMENT_NOT_FOUND, ex.getErrorCode());
	}

	@Test
	void 나의_댓글이_아니면_실패한다() {
		// given
		DeleteMyCommentCommand command = new DeleteMyCommentCommand(2L, 1L);

		Member member = Mockito.mock(Member.class);
		given(member.getId()).willReturn(1L);

		Comment comment = Mockito.mock(Comment.class);
		given(comment.getMember()).willReturn(member);

		given(commentRepository.findWithAuthorById(1L)).willReturn(Optional.of(comment));

		// when & then
		BusinessException ex = assertThrows(BusinessException.class, () -> deleteMyCommentUseCase.execute(command));
		assertEquals(CommentErrorCode.COMMENT_FORBIDDEN, ex.getErrorCode());
	}
}
