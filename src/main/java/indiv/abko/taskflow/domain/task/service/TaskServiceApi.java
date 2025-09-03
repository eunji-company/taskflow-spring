package indiv.abko.taskflow.domain.task.service;

import indiv.abko.taskflow.domain.task.entity.Task;

public interface TaskServiceApi {
	// TODO 작업해주세요~
	default Task getByIdOrThrow(long taskId) {
		// 기본키를 사용하여 Task를 가져온다
		// 없으면 예외처리
		return null;
	}
}
