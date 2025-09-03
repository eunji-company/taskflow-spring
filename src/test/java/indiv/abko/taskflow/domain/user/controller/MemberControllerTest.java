package indiv.abko.taskflow.domain.user.controller;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.auditing.AuditingHandler;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import indiv.abko.taskflow.domain.user.dto.MemberInfoResponse;
import indiv.abko.taskflow.domain.user.entity.Member;
import indiv.abko.taskflow.domain.user.entity.UserRole;
import indiv.abko.taskflow.domain.user.service.ViewMemberInfoUseCase;
import indiv.abko.taskflow.global.auth.WithMockAuthMember;

@WebMvcTest(controllers = MemberController.class)
public class MemberControllerTest {
	@Autowired
	MockMvc mockMvc;

	@MockitoBean
	ViewMemberInfoUseCase viewMemberInfoUseCase;

	@MockitoBean(name = "jpaMappingContext")
	JpaMetamodelMappingContext jpaMetamodelMappingContext;

	@MockitoBean(name = "jpaAuditingHandler")
	AuditingHandler jpaAuditingHandler;

	@Test
	@WithMockAuthMember(memberId = 1L, userRole = UserRole.USER)
	@DisplayName("GET /api/users/me - 인증된 사용자의 정보를 반환한다")
	public void getMemberInfo_성공() throws Exception {
		//given
		Long memberId = 1L;
		Member member = Member.createForTest("testusername", "HASHED_PW", "test@example.com", "testname",
			UserRole.USER);
		var dto = new MemberInfoResponse(member);

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
}
