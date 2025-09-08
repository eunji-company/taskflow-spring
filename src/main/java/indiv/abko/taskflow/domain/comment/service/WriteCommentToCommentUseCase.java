package indiv.abko.taskflow.domain.comment.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import indiv.abko.taskflow.domain.comment.dto.command.WriteCommentToCommentCommand;
import indiv.abko.taskflow.domain.comment.dto.response.WriteCommentToCommentResponse;
import indiv.abko.taskflow.domain.comment.entity.Comment;
import indiv.abko.taskflow.domain.comment.exception.CommentErrorCode;
import indiv.abko.taskflow.domain.comment.mapper.CommentMapper;
import indiv.abko.taskflow.domain.comment.repository.CommentRepository;
import indiv.abko.taskflow.domain.task.entity.Task;
import indiv.abko.taskflow.domain.task.service.TaskServiceApi;
import indiv.abko.taskflow.domain.user.entity.Member;
import indiv.abko.taskflow.domain.user.service.MemberServiceApi;
import indiv.abko.taskflow.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WriteCommentToCommentUseCase {
    private final MemberServiceApi memberServiceApi;
    private final TaskServiceApi taskServiceApi;
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    @Transactional
    public WriteCommentToCommentResponse execute(WriteCommentToCommentCommand command) {
        Member author = memberServiceApi.getByIdOrThrow(command.authorId());
        Task task = taskServiceApi.getByIdOrThrow(command.taskId());
        Comment comment = commentRepository.findById(command.parentCommentId())
            .orElseThrow(() -> new BusinessException(CommentErrorCode.COMMENT_NOT_FOUND));

        Comment newComment = Comment.of(author, task, comment, command.content());
        Comment savedComment = commentRepository.save(newComment);
        return commentMapper.toWriteCommentToCommentResponse(author, savedComment.getTask().getId(), comment.getId(),
            savedComment);
    }
}
