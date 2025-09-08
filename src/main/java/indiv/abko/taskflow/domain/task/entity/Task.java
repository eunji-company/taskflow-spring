package indiv.abko.taskflow.domain.task.entity;

import java.time.LocalDateTime;

import indiv.abko.taskflow.domain.task.dto.response.UpdateTaskResponse;
import indiv.abko.taskflow.domain.user.entity.Member;
import indiv.abko.taskflow.global.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tasks")
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Task extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(optional = false)
	@JoinColumn(nullable = false)
	private Member member;

	@Column(nullable = false)
	private String title;

	@Column(nullable = false)
	private String description;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private TaskStatus status;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private TaskPriority priority;

	@Column(nullable = false)
	private LocalDateTime dueDate;

	private LocalDateTime deletedAt;

	public Task(String title, String description, LocalDateTime dueDate, TaskPriority priority, Member member, TaskStatus status) {
		this.title = title;
		this.description = description;
		this.dueDate = dueDate;
		this.priority = priority;
		this.member = member;
		this.status = status;
	}

    public void updateTask(String title, String description, LocalDateTime dueDate, TaskPriority priority, Member member) {
		this.title = title;
		this.description = description;
		this.dueDate = dueDate;
		this.priority = priority;
		this.member = member;
	}

	public void updateStatus(TaskStatus status) {
		this.status = status;
	}
}
