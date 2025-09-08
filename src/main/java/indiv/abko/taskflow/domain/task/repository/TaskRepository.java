package indiv.abko.taskflow.domain.task.repository;

import indiv.abko.taskflow.domain.task.entity.Task;
import indiv.abko.taskflow.domain.task.entity.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {

	Page<Task> findAllByStatusAndMemberId(TaskStatus status, Long memberId, Pageable pageable);

	Page<Task> findAllByStatus(TaskStatus status, Pageable pageable);

	Page<Task> findAllByMemberId(Long memberId, Pageable pageable);
}
