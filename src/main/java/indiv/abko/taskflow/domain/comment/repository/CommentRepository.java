package indiv.abko.taskflow.domain.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import indiv.abko.taskflow.domain.comment.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
