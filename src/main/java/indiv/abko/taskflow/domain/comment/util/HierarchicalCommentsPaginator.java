package indiv.abko.taskflow.domain.comment.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import indiv.abko.taskflow.domain.comment.dto.CommentPaginationData;
import indiv.abko.taskflow.domain.comment.entity.Comment;

public class HierarchicalCommentsPaginator {
    private final List<Comment> parentComments;
    private final List<Comment> childComments;

    private Map<Long, List<Comment>> childCommentsMappingToParent = new HashMap<>();

    public HierarchicalCommentsPaginator(CommentPaginationData commentPaginationData) {
        this.parentComments = commentPaginationData.parentComments();
        this.childComments = commentPaginationData.childComments();
    }

    public PagedComments paginate(PaginationContext paginationContext) {
        childCommentsMappingToParent = childComments.stream()
            .collect(Collectors.groupingBy(comment -> comment.getParentComment().getId()));
        List<Comment> pagedComments = paginateHierarchicalComments(paginationContext);
        return calculatePageInfos(pagedComments, paginationContext);
    }

    private List<Comment> paginateHierarchicalComments(PaginationContext paginationContext) {
        List<Comment> pagedComments = new ArrayList<>();

        for (Comment parent : parentComments) {
            if (paginationContext.isInRange()) {
                pagedComments.add(parent);
                paginationContext.next();
            } else {
                paginationContext.next();
            }

            if (paginationContext.shouldStop()) {
                return pagedComments;
            }

            List<Comment> childComments = getChildComments(parent);
            for (Comment child : childComments) {
                if (paginationContext.isInRange()) {
                    pagedComments.add(child);
                }
                paginationContext.next();

                if (paginationContext.shouldStop()) {
                    return pagedComments;
                }
            }
        }

        return pagedComments;
    }

    private PagedComments calculatePageInfos(List<Comment> pagedComments, PaginationContext paginationContext) {
        long totalElements = parentComments.size() + childComments.size();
        int totalPages = (int)Math.ceil((double)totalElements / paginationContext.getSize());
        int size = pagedComments.size();
        int number = paginationContext.getNumber();
        return new PagedComments(pagedComments, totalElements, totalPages, size, number);
    }

    private List<Comment> getChildComments(Comment parent) {
        return childCommentsMappingToParent.getOrDefault(parent.getId(), List.of());
    }

    public record PagedComments(
        List<Comment> comments,
        long totalElements,
        int totalPages,
        int size,
        int number) {
    }
}
