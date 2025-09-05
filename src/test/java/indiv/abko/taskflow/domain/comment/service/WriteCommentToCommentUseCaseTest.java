package indiv.abko.taskflow.domain.comment.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import indiv.abko.taskflow.domain.comment.dto.command.WriteCommentToCommentCommand;
import indiv.abko.taskflow.domain.comment.dto.response.WriteCommentToCommentResponse;
import indiv.abko.taskflow.domain.comment.entity.Comment;
import indiv.abko.taskflow.domain.comment.mapper.CommentMapper;
import indiv.abko.taskflow.domain.comment.repository.CommentRepository;
import indiv.abko.taskflow.domain.task.entity.Task;
import indiv.abko.taskflow.domain.user.entity.Member;
import indiv.abko.taskflow.domain.user.entity.UserRole;
import indiv.abko.taskflow.domain.user.service.MemberServiceApi;

@ExtendWith(MockitoExtension.class)
public class WriteCommentToCommentUseCaseTest {
    @Mock
    private MemberServiceApi memberServiceApi;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private CommentMapper commentMapper;

    @InjectMocks
    private WriteCommentToCommentUseCase writeCommentToCommentUseCase;

    @Test
    void 대댓글을_작성한다() {
        // given
        WriteCommentToCommentCommand command = new WriteCommentToCommentCommand(1L, 1L, 1L,
            "null");
        Member member = Member.of("null", "null", "null", "null", UserRole.USER);
        Task task = Mockito.mock(Task.class);
        Comment parentComment = Comment.of(member, task, "null");
        ReflectionTestUtils.setField(parentComment, "id", 1L);
        Comment newComment = Comment.of(member, parentComment, "null");
        var resp = WriteCommentToCommentResponse.builder()
            .id(1L)
            .content("null")
            .createdAt(null)
            .parentId(1L)
            .updatedAt(null)
            .user(new WriteCommentToCommentResponse.UserResp(
                1L,
                "null",
                "null",
                "null",
                "null"))
            .userId(1L)
            .build();

        given(memberServiceApi.getByIdOrThrow(anyLong())).willReturn(member);
        given(commentRepository.findById(anyLong())).willReturn(Optional.of(parentComment));
        given(commentRepository.save(any())).willReturn(newComment);
        given(commentMapper.toWriteCommentToCommentResponse(any(), anyLong(), anyLong(), any()))
            .willReturn(resp);

        // when
        writeCommentToCommentUseCase.execute(command);

        // then
        then(commentRepository).should(times(1)).save(any(Comment.class));
    }
}
