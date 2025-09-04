package indiv.abko.taskflow.domain.user.controller;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import indiv.abko.taskflow.domain.user.dto.MemberInfoResponse;
import indiv.abko.taskflow.domain.user.dto.MembersInfoResponse;
import indiv.abko.taskflow.domain.user.entity.Member;
import indiv.abko.taskflow.domain.user.entity.UserRole;
import indiv.abko.taskflow.domain.user.service.ViewMemberInfoUseCase;
import indiv.abko.taskflow.domain.user.service.ViewMembersInfoUseCase;
import indiv.abko.taskflow.global.auth.WithMockAuthMember;

@WebMvcTest(controllers = MemberController.class)
public class MemberControllerTest {
	@Autowired
	MockMvc mockMvc;

	@MockitoBean
	ViewMemberInfoUseCase viewMemberInfoUseCase;

	@MockitoBean
	ViewMembersInfoUseCase viewMembersInfoUseCase;

	@Test
	@WithMockAuthMember(memberId = 1L, userRole = UserRole.USER)
	@DisplayName("GET /api/users/me - 인증된 사용자의 정보를 반환한다")
	public void getMember_성공() throws Exception {
		//given
		Long memberId = 1L;
		Member member = Member.of("testusername", "HASHED_PW", "test@example.com", "testname", UserRole.USER);

		var dto = new MemberInfoResponse(member.getId(), member.getUsername(), member.getEmail(), member.getName(),
			member.getUserRole().name(), member.getCreatedAt());

		given(viewMemberInfoUseCase.execute(memberId)).willReturn(dto);

		//when & then
		mockMvc.perform(get("/api/users/me"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.success").value(true))
			.andExpect(jsonPath("$.message").value("사용자 정보를 조회했습니다."))
			.andExpect(jsonPath("$.data.id").value(member.getId()))
			.andExpect(jsonPath("$.data.username").value("testusername"))
			.andExpect(jsonPath("$.data.email").value("test@example.com"))
			.andExpect(jsonPath("$.data.name").value("testname"))
			.andExpect(jsonPath("$.data.role").value("USER"))
			.andExpect(jsonPath("$.timestamp").exists());

		verify(viewMemberInfoUseCase, times(1)).execute(memberId);
	}

	@Test
	@WithMockAuthMember(memberId = 1L, userRole = UserRole.USER)
	@DisplayName("GET /api/users/ - 모든 사용자의 정보를 반환한다")
	public void getMembers_성공() throws Exception {
		//given
		Member member1 = Member.of("testusername", "HASHED_PW", "test@example.com", "testname", UserRole.USER);
		Member member2 = Member.of("testusername2", "HASHED_PW2", "test2@example.com", "testname2", UserRole.ADMIN);
		ReflectionTestUtils.setField(member1, "id", 1L);
		ReflectionTestUtils.setField(member2, "id", 2L);

		var dtoList = List.of(new MembersInfoResponse(member1.getId(), member1.getEmail(), member1.getName(),
				member1.getUserRole().name()),
			new MembersInfoResponse(member2.getId(), member2.getEmail(), member2.getName(),
				member2.getUserRole().name()));
		given(viewMembersInfoUseCase.execute()).willReturn(dtoList);

		//when & then
		mockMvc.perform(get("/api/users/"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.success").value(true))
			.andExpect(jsonPath("$.message").value("요청이 성공적으로 처리되었습니다."))
			.andExpect(jsonPath("$.data").isArray())
			.andExpect(jsonPath("$.data.length()").value(2))

			.andExpect(jsonPath("$.data[0].id").value(1))
			.andExpect(jsonPath("$.data[0].email").value("test@example.com"))
			.andExpect(jsonPath("$.data[0].name").value("testname"))
			.andExpect(jsonPath("$.data[0].role").value("USER"))

			.andExpect(jsonPath("$.data[1].id").value(2))
			.andExpect(jsonPath("$.data[1].email").value("test2@example.com"))
			.andExpect(jsonPath("$.data[1].name").value("testname2"))
			.andExpect(jsonPath("$.data[1].role").value("ADMIN"))

			.andExpect(jsonPath("$.timestamp").exists());

		verify(viewMembersInfoUseCase, times(1)).execute();
	}

}
