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

import indiv.abko.taskflow.domain.auth.dto.command.LoginCommand;
import indiv.abko.taskflow.domain.auth.dto.command.RegisterCommand;
import indiv.abko.taskflow.domain.auth.dto.command.WithdrawCommand;
import indiv.abko.taskflow.domain.auth.dto.request.LoginRequest;
import indiv.abko.taskflow.domain.auth.dto.request.RegisterRequest;
import indiv.abko.taskflow.domain.auth.dto.request.WithdrawRequest;
import indiv.abko.taskflow.domain.auth.dto.response.LoginResponse;
import indiv.abko.taskflow.domain.auth.dto.response.RegisterResponse;
import indiv.abko.taskflow.domain.auth.mapper.AuthMapper;
import indiv.abko.taskflow.domain.auth.service.LoginUseCase;
import indiv.abko.taskflow.domain.auth.service.RegisterUseCase;
import indiv.abko.taskflow.domain.auth.service.WithdrawUseCase;
import indiv.abko.taskflow.global.auth.AuthMember;
import indiv.abko.taskflow.global.jwt.JwtAuthenticationToken;
import indiv.abko.taskflow.support.ControllerTestSupport;

@WebMvcTest(AuthController.class)
public class AuthControllerTest extends ControllerTestSupport {

	@MockitoBean
	private RegisterUseCase registerUseCase;

	@MockitoBean
	private AuthMapper authMapper;

	@MockitoBean
	private LoginUseCase loginUseCase;

	@MockitoBean
	private WithdrawUseCase withdrawUseCase;

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
						.content(objectMapper.writeValueAsString(request)))
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
						.content(objectMapper.writeValueAsString(request)))
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
						.content(objectMapper.writeValueAsString(request)))
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
						.content(objectMapper.writeValueAsString(request)))
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
						.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isBadRequest());
		}
	}

	@Nested
	class Login {
		private static final String BASE_URL = "/api/auth/login";

		@Test
		void 로그인에_성공한다() throws Exception {
			// given
			LoginRequest request = new LoginRequest("test", "password123!");
			LoginCommand command = new LoginCommand("test", "password123!");
			LoginResponse response = new LoginResponse("testAccessToken");

			given(authMapper.toLoginCommand(any(LoginRequest.class))).willReturn(command);
			given(loginUseCase.execute(command)).willReturn(response);

			// when & then
			mockMvc.perform(
					post(BASE_URL)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request))
				)
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.data.token").value("testAccessToken"));
		}
	}

	@Nested
	class Withdraw {
		private static final String BASE_URL = "/api/auth/withdraw";

		@Test
		void 회원탈퇴에_성공한다() throws Exception {
			// given
			WithdrawRequest request = new WithdrawRequest("password123!");
			WithdrawCommand command = new WithdrawCommand(1L, "password123!", "testAccessToken");

			given(authMapper.toWithdrawCommand(any(AuthMember.class), any(WithdrawRequest.class), anyString()))
				.willReturn(command);
			willDoNothing().given(withdrawUseCase).execute(command);

			given(jwtUtil.validateToken(anyString())).willReturn(true);

			AuthMember authMember = new AuthMember(1L);
			JwtAuthenticationToken authenticationToken = new JwtAuthenticationToken(authMember, null, null);
			given(jwtAuthenticationConverter.convert(any())).willReturn(authenticationToken);

			// when & then
			mockMvc.perform(
					post(BASE_URL)
						.header("Authorization", "Bearer testAccessToken")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.message").value("회원탈퇴가 완료되었습니다."));
		}
	}

}
