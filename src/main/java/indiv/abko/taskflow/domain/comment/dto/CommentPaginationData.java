package indiv.abko.taskflow.domain.comment.dto;

import java.util.List;

import indiv.abko.taskflow.domain.comment.entity.Comment;

public record CommentPaginationData(List<Comment> parentComments, List<Comment> childComments) {
}
