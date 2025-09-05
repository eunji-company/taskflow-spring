package indiv.abko.taskflow.domain.team.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import indiv.abko.taskflow.domain.team.dto.response.AddTeamMemberResponse;
import indiv.abko.taskflow.domain.team.entity.Team;
import indiv.abko.taskflow.domain.team.entity.TeamMemberId;
import indiv.abko.taskflow.domain.team.repository.TeamMemberRepository;
import indiv.abko.taskflow.domain.team.repository.TeamRepository;
import indiv.abko.taskflow.domain.user.entity.Member;
import indiv.abko.taskflow.domain.user.entity.UserRole;
import indiv.abko.taskflow.domain.user.service.MemberServiceApi;

@ExtendWith(MockitoExtension.class)
public class AddTeamMemberUseCaseTest {
	@Mock
	private TeamRepository teamRepository;

	@Mock
	private TeamMemberRepository teamMemberRepository;

	@Mock
	private MemberServiceApi memberServiceApi;

	@InjectMocks
	private AddTeamMemberUseCase useCase;

	@Test
	public void 팀에_멤버를_추가할_수_있다() {
		// given
		Member member = Member.of("testUser", "12345678", "test@example.com", "홍길동", UserRole.USER);
		ReflectionTestUtils.setField(member, "id", 1L);

		Team team = new Team("개발팀", "");
		ReflectionTestUtils.setField(team, "id", 1L);

		given(memberServiceApi.getByIdOrThrow(anyLong())).willReturn(member);
		given(teamRepository.findWithTeamMembersById(anyLong())).willReturn(Optional.of(team));
		given(teamMemberRepository.existsById(any(TeamMemberId.class))).willReturn(false);

		// when
		AddTeamMemberResponse addTeamMemberResponse = useCase.execute(1L, 1L);

		// then
		assertThat(addTeamMemberResponse).isNotNull();
		assertThat(addTeamMemberResponse.id()).isEqualTo(1L);
		assertThat(addTeamMemberResponse.name()).isEqualTo("개발팀");
		assertThat(addTeamMemberResponse.members()).hasSize(1);

		AddTeamMemberResponse.UserResp userResp = addTeamMemberResponse.members().get(0);
		assertThat(userResp.id()).isEqualTo(1L);
		assertThat(userResp.username()).isEqualTo("testUser");
		assertThat(userResp.name()).isEqualTo("홍길동");
		assertThat(userResp.email()).isEqualTo("test@example.com");
		assertThat(userResp.role()).isEqualTo("USER");

		verify(memberServiceApi).getByIdOrThrow(1L);
		verify(teamRepository).findWithTeamMembersById(1L);
		verify(teamMemberRepository).existsById(any(TeamMemberId.class));
	}
}
