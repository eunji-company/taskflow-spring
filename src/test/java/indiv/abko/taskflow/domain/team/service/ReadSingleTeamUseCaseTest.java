package indiv.abko.taskflow.domain.team.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import indiv.abko.taskflow.domain.team.dto.response.ReadSingleTeamResponse;
import indiv.abko.taskflow.domain.team.entity.Team;
import indiv.abko.taskflow.domain.team.repository.TeamRepository;

@ExtendWith(MockitoExtension.class)
public class ReadSingleTeamUseCaseTest {
	@Mock
	private TeamRepository teamRepository;

	@InjectMocks
	private ReadSingleTeamUseCase useCase;

	@Test
	public void 특정_팀_조회에_성공한다() {
		// given
		Team team = new Team("Dev Team", "개발팀");
		ReflectionTestUtils.setField(team, "id", 1L);

		given(teamRepository.findWithTeamMembersById(anyLong())).willReturn(Optional.of(team));

		// when
		ReadSingleTeamResponse readSingleTeamResponse = useCase.execute(1L);

		// then
		assertThat(readSingleTeamResponse).isNotNull();
		assertThat(readSingleTeamResponse.id()).isEqualTo(1L);
		assertThat(readSingleTeamResponse.name()).isEqualTo("Dev Team");
		assertThat(readSingleTeamResponse.description()).isEqualTo("개발팀");
		assertThat(readSingleTeamResponse.members()).isEmpty();
	}
}
