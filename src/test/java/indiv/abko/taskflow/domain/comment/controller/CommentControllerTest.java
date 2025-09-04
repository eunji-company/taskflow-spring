package indiv.abko.taskflow.domain.comment.controller;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.Instant;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.auditing.AuditingHandler;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import indiv.abko.taskflow.domain.comment.dto.command.WriteCommentToTaskCommand;
import indiv.abko.taskflow.domain.comment.dto.request.WriteCommentRequest;
import indiv.abko.taskflow.domain.comment.dto.response.WriteCommentToTaskResponse;
import indiv.abko.taskflow.domain.comment.mapper.CommentMapper;
import indiv.abko.taskflow.domain.comment.service.WriteCommentToTaskUseCase;
import indiv.abko.taskflow.domain.user.entity.UserRole;
import indiv.abko.taskflow.global.auth.AuthMember;
import indiv.abko.taskflow.global.auth.WithMockAuthMember;

@WebMvcTest(CommentController.class)
public class CommentControllerTest {
	@MockitoBean(name = "jpaMappingContext")
	JpaMetamodelMappingContext jpaMetamodelMappingContext;

	@MockitoBean(name = "jpaAuditingHandler")
	AuditingHandler jpaAuditingHandler;

	@MockitoBean
	private WriteCommentToTaskUseCase writeCommentToTaskUseCase;

	@MockitoBean
	private CommentMapper commentMapper;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private MockMvc mockMvc;

	@Test
	@WithMockAuthMember(memberId = 1L, userRole = UserRole.USER)
	void task에_comment를_작성할수_있다() throws Exception {
		// given
		WriteCommentRequest request = new WriteCommentRequest("내용", null);
		WriteCommentToTaskCommand command = new WriteCommentToTaskCommand(1L, 1L, "내용");
		WriteCommentToTaskResponse resp = new WriteCommentToTaskResponse(1L, "내용", 1L, 1L, null, 1L, Instant.now(),
			Instant.now());

		given(commentMapper.toWriteCommentToTaskCommand(any(AuthMember.class), anyLong(),
			any(WriteCommentRequest.class))).willReturn(command);
		given(writeCommentToTaskUseCase.execute(command)).willReturn(resp);

		// when & then
		mockMvc.perform(post("/api/tasks/1/comments")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isCreated());
	}
}
