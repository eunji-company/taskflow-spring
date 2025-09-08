package indiv.abko.taskflow.domain.comment.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import indiv.abko.taskflow.domain.comment.dto.command.WriteCommentToTaskCommand;
import indiv.abko.taskflow.domain.comment.dto.response.WriteCommentToTaskResponse;
import indiv.abko.taskflow.domain.comment.entity.Comment;
import indiv.abko.taskflow.domain.comment.mapper.CommentMapper;
import indiv.abko.taskflow.domain.comment.repository.CommentRepository;
import indiv.abko.taskflow.domain.task.entity.Task;
import indiv.abko.taskflow.domain.task.service.TaskServiceApi;
import indiv.abko.taskflow.domain.user.entity.Member;
import indiv.abko.taskflow.domain.user.service.MemberServiceApi;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WriteCommentToTaskUseCase {
	private final CommentRepository commentRepository;
	private final MemberServiceApi memberService;
	private final TaskServiceApi taskService;
	private final CommentMapper commentMapper;

	@Transactional
	public WriteCommentToTaskResponse execute(WriteCommentToTaskCommand command) {
		Member member = memberService.getByIdOrThrow(command.memberId());
		Task task = taskService.getByIdOrThrow(command.taskId());
		Comment comment = Comment.of(member, task, command.content());
		Comment savedComment = commentRepository.save(comment);
		return commentMapper.toWriteCommentToTaskResponse(member, task, savedComment);
	}
}
