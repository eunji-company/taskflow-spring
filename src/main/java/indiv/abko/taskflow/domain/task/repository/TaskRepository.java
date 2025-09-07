package indiv.abko.taskflow.domain.task.repository;

import indiv.abko.taskflow.domain.task.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
	Page<Task> findAllByMemberId(long l, Pageable pageable);
}
