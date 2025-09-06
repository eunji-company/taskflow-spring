package indiv.abko.taskflow.domain.comment.dto.command;

import org.springframework.data.domain.Pageable;

import indiv.abko.taskflow.domain.comment.enums.CommentSortOption;

public record ViewCommentsFromTaskCommand(Pageable pageable, long taskid, CommentSortOption sortOption) {

}
