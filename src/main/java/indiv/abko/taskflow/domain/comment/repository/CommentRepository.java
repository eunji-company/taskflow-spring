package indiv.abko.taskflow.domain.comment.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import indiv.abko.taskflow.domain.comment.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
	@EntityGraph(attributePaths = {"member"})
	Optional<Comment> findWithAuthorById(Long id);

	void deleteAllByParentComment(Comment parentComment);

	@EntityGraph(attributePaths = {"member", "task"})
	List<Comment> findWithDetailsByTaskIdOrderByCreatedAtDesc(long taskId);

	@EntityGraph(attributePaths = {"member", "task"})
	List<Comment> findWithDetailsByTaskIdOrderByCreatedAt(long taskId);

	@EntityGraph(attributePaths = {"member", "parentComment"})
	List<Comment> findWithDetailByParentCommentIsInOrderByCreatedAt(List<Comment> parentComments);
}
