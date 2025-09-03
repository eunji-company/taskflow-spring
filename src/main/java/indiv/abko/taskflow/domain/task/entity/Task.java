package indiv.abko.taskflow.domain.task.entity;

import java.time.LocalDateTime;

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
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tasks")
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
}
