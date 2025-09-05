package indiv.abko.taskflow.domain.auth.controller;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.Instant;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import indiv.abko.taskflow.domain.auth.dto.command.RegisterCommand;
import indiv.abko.taskflow.domain.auth.dto.request.RegisterRequest;
import indiv.abko.taskflow.domain.auth.dto.response.RegisterResponse;
import indiv.abko.taskflow.domain.auth.mapper.AuthMapper;
import indiv.abko.taskflow.domain.auth.service.LoginUseCase;
import indiv.abko.taskflow.domain.auth.service.RegisterUseCase;
import indiv.abko.taskflow.support.ControllerTestSupport;

@WebMvcTest(AuthController.class)
public class AuthControllerTest extends ControllerTestSupport {

	@MockitoBean
	private RegisterUseCase registerUseCase;

	@MockitoBean
	private LoginUseCase loginUseCase;

	@MockitoBean
	private AuthMapper authMapper;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private MockMvc mockMvc;

	@Nested
	class Register {
		private static final String BASE_URL = "/api/auth/register";

		@Test
		void 회원가입에_성공한다() throws Exception {
			// given
			RegisterRequest request = new RegisterRequest("test", "password123!", "test@test.com", "testName");
			RegisterCommand command = new RegisterCommand("test", "password123!", "test@test.com", "testName");
			RegisterResponse response = new RegisterResponse(1L, "test", "test@test.com", "testName", "USER",
				Instant.now());

			given(authMapper.toRegisterCommand(any(RegisterRequest.class))).willReturn(command);
			given(registerUseCase.execute(command)).willReturn(response);

			// when & then
			mockMvc.perform(
					post(BASE_URL)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request))
				)
				.andExpect(status().isOk());
		}

		@Test
		void 아이디_검증에_실패하여_회원가입에_실패한다() throws Exception {
			// given
			RegisterRequest request = new RegisterRequest("", "password123!", "test@test.com", "testName");

			// when & then
			mockMvc.perform(
					post(BASE_URL)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request))
				)
				.andExpect(status().isBadRequest());
		}

		@Test
		void 이메일_검증에_실패하여_회원가입에_실패한다() throws Exception {
			// given
			RegisterRequest request = new RegisterRequest("test", "password123!", "test", "testName");

			// when & then
			mockMvc.perform(
					post(BASE_URL)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request))
				)
				.andExpect(status().isBadRequest());
		}

		@Test
		void 비밀번호_검증에_실패하여_회원가입에_실패한다() throws Exception {
			// given
			RegisterRequest request = new RegisterRequest("test", "pass", "test@test.com", "testName");

			// when & then
			mockMvc.perform(
					post(BASE_URL)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request))
				)
				.andExpect(status().isBadRequest());
		}

		@Test
		void 이름_검증에_실패하여_회원가입에_실패한다() throws Exception {
			// given
			RegisterRequest request = new RegisterRequest("test", "password123!", "test@test.com", "");

			// when & then
			mockMvc.perform(
					post(BASE_URL)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request))
				)
				.andExpect(status().isBadRequest());
		}
	}
}
