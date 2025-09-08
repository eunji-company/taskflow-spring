package indiv.abko.taskflow.domain.comment.util;

import org.springframework.stereotype.Component;
import indiv.abko.taskflow.domain.comment.dto.CommentPaginationData;

@Component
public class HierarchicalCommentsPaginatorFactory {
    public HierarchicalCommentsPaginator create(CommentPaginationData commentPaginationData) {
        return new HierarchicalCommentsPaginator(commentPaginationData);
    }
}