package indiv.abko.taskflow.domain.comment.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import indiv.abko.taskflow.domain.comment.dto.CommentPaginationData;
import indiv.abko.taskflow.domain.comment.dto.command.ViewCommentsFromTaskCommand;
import indiv.abko.taskflow.domain.comment.dto.response.ViewCommentsFromTaskResponse;
import indiv.abko.taskflow.domain.comment.entity.Comment;
import indiv.abko.taskflow.domain.comment.enums.CommentSortOption;
import indiv.abko.taskflow.domain.comment.mapper.CommentMapper;
import indiv.abko.taskflow.domain.comment.repository.CommentRepository;
import indiv.abko.taskflow.domain.comment.util.HierarchicalCommentsPaginator;
import indiv.abko.taskflow.domain.comment.util.HierarchicalCommentsPaginatorFactory;
import indiv.abko.taskflow.domain.comment.util.PaginationContext;
import indiv.abko.taskflow.domain.comment.util.HierarchicalCommentsPaginator.PagedComments;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ViewCommentsFromTaskUseCase {
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final HierarchicalCommentsPaginatorFactory paginatorFactory;

    @Transactional(readOnly = true)
    public ViewCommentsFromTaskResponse execute(ViewCommentsFromTaskCommand command) {
        // 중요: 댓글은 계층적으로 정렬되어 반환됩니다. 
        // 부모 댓글들이 먼저 정렬되고, 
        // 각 부모 댓글 바로 다음에 해당 대댓글들이 시간순으로 배치됩니다.

        List<Comment> parentComments = getParentComments(command.sortOption(), command.taskid());
        List<Comment> childComments = commentRepository
            .findWithDetailByParentCommentIsInOrderByCreatedAt(parentComments);

        CommentPaginationData commentPaginationData = new CommentPaginationData(parentComments, childComments);
        HierarchicalCommentsPaginator paginator = paginatorFactory.create(commentPaginationData);
        PaginationContext paginationContext = new PaginationContext(command.pageable());

        PagedComments results = paginator.paginate(paginationContext);
        return commentMapper.toViewCommentsFromTaskResponse(results);
    }

    private List<Comment> getParentComments(CommentSortOption sortOption, long taskId) {
        if (sortOption == CommentSortOption.NEWEST) {
            return commentRepository.findWithDetailsByTaskIdAndParentCommentIsNullOrderByCreatedAtDesc(taskId);
        } else {
            return commentRepository.findWithDetailsByTaskIdAndParentCommentIsNullOrderByCreatedAt(taskId);
        }
    }
}
