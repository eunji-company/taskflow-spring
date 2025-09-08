package indiv.abko.taskflow.domain.task.controller;

import indiv.abko.taskflow.domain.task.dto.response.AssigneeResponse;
import indiv.abko.taskflow.domain.task.dto.response.CreateTaskResponse;
import indiv.abko.taskflow.domain.task.repository.TaskRepository;
import indiv.abko.taskflow.domain.task.service.CreateTaskUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static indiv.abko.taskflow.domain.task.entity.TaskPriority.MEDIUM;
import static indiv.abko.taskflow.domain.task.entity.TaskStatus.TODO;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@WebMvcTest(controllers = TaskController.class)
//public class TaskControllerTest {
//
//	@Autowired
//	private MockMvc mockMvc;
//
//	@MockitoBean
//	CreateTaskUseCase createTaskUseCase;
//
//	@MockitoBean
//	TaskRepository taskRepository;
//
//	@Test
//	@WithMockUser(username = "testUser")	//가짜 로그인 유저 추가
//	void task_생성_성공() throws Exception {
//		//given
//		LocalDateTime dueDate = LocalDateTime.now().plusDays(7);
//		LocalDateTime createdAt = LocalDateTime.now();
//		LocalDateTime modifiedAt = LocalDateTime.now();
//		CreateTaskResponse response = new CreateTaskResponse(
//				1L,
//				"제목테스트",
//				"작업내용테스트",
//				dueDate,
//				MEDIUM,
//				TODO,
//				1L,
//				new AssigneeResponse(1L, "loginName", "홍길동", "gildong@gmail.com"),
//				createdAt,
//				modifiedAt
//		);
//		given(createTaskUseCase.createTask(any())).willReturn(response);
//
//		//when & then
//		mockMvc.perform(post("/api/tasks")
//						.with(csrf())	//시큐리티 CSRF 토큰 추가
//				.contentType(String.valueOf(MediaType.APPLICATION_JSON))
//				.content("""
//				{
//                    "title": "제목테스트",
//                    "description": "작업내용테스트",
//                    "priority": "MEDIUM",
//                    "dueDate": "2025-09-04T18:29:54.367178"
//                }
//            """))
//				.andExpect(status().isCreated())
//				.andExpect(jsonPath("$.data.title").value("제목테스트"))
//				.andExpect(jsonPath("$.data.priority").value("MEDIUM"))
//				.andExpect(jsonPath("$.data.assigneeResponse.name").value("홍길동"));
//	}
//}
