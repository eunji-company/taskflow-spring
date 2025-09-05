package indiv.abko.taskflow.domain.task.repository;

import indiv.abko.taskflow.domain.task.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
