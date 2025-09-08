package indiv.abko.taskflow.domain.team.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import indiv.abko.taskflow.domain.team.dto.response.ReadTeamMembersResponse;
import indiv.abko.taskflow.domain.team.entity.Team;
import indiv.abko.taskflow.domain.team.repository.TeamRepository;
import indiv.abko.taskflow.domain.user.entity.Member;
import indiv.abko.taskflow.domain.user.entity.UserRole;

@ExtendWith(MockitoExtension.class)
public class ReadTeamMembersUseCaseTest {
	@Mock
	private TeamRepository teamRepository;

	@InjectMocks
	private ReadTeamMembersUseCase useCase;

	@Test
	public void 팀_멤버_목록을_조회할_수_있다() {
		// given
		Member member = Member.of("testUser", "12345678", "test@example.com", "홍길동", UserRole.USER);

		ReflectionTestUtils.setField(member, "createdAt", LocalDateTime.now());
		ReflectionTestUtils.setField(member, "id", 1L);

		Team team = new Team("Dev Team", "개발팀");

		ReflectionTestUtils.setField(team, "createdAt", LocalDateTime.now());
		ReflectionTestUtils.setField(team, "id", 1L);

		team.addMember(member);

		given(teamRepository.findWithTeamMembersById(anyLong())).willReturn(Optional.of(team));

		// when
		List<ReadTeamMembersResponse> readTeamMembersResponses = useCase.execute(1L);

		// then
		assertThat(readTeamMembersResponses).hasSize(1);
		assertThat(readTeamMembersResponses.get(0).id()).isEqualTo(1L);
		assertThat(readTeamMembersResponses.get(0).username()).isEqualTo("testUser");
	}
}
