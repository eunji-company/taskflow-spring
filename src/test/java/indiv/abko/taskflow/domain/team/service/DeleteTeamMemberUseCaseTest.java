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

import indiv.abko.taskflow.domain.team.dto.response.DeleteTeamMemberResponse;
import indiv.abko.taskflow.domain.team.entity.Team;
import indiv.abko.taskflow.domain.team.entity.TeamMemberId;
import indiv.abko.taskflow.domain.team.repository.TeamMemberRepository;
import indiv.abko.taskflow.domain.team.repository.TeamRepository;
import indiv.abko.taskflow.domain.user.entity.Member;
import indiv.abko.taskflow.domain.user.entity.UserRole;
import indiv.abko.taskflow.domain.user.service.MemberServiceApi;

@ExtendWith(MockitoExtension.class)
public class DeleteTeamMemberUseCaseTest {
	@Mock
	private TeamRepository teamRepository;

	@Mock
	private TeamMemberRepository teamMemberRepository;

	@Mock
	private MemberServiceApi memberServiceApi;

	@InjectMocks
	private DeleteTeamMemberUseCase useCase;

	@Test
	public void 팀_멤버를_삭제할_수_있다() {
		// given
		Member member = Member.of("testUser", "12345678", "test@example.com", "홍길동", UserRole.USER);
		ReflectionTestUtils.setField(member, "id", 1L);

		Team team = new Team("개발팀", "");
		ReflectionTestUtils.setField(team, "id", 1L);

		team.addMember(member);

		given(memberServiceApi.getByIdOrThrow(anyLong())).willReturn(member);
		given(teamRepository.findWithTeamMembersById(anyLong())).willReturn(Optional.of(team));
		given(teamMemberRepository.existsById(any(TeamMemberId.class))).willReturn(true);

		// when
		DeleteTeamMemberResponse deleteTeamMemberResponse = useCase.execute(1L, 1L);

		// then
		assertThat(deleteTeamMemberResponse).isNotNull();
		assertThat(deleteTeamMemberResponse.members()).isEmpty();
		assertThat(team.getTeamMembers()).isEmpty();
	}
}
