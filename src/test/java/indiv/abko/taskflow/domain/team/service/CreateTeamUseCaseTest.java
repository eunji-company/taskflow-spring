package indiv.abko.taskflow.domain.team.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import indiv.abko.taskflow.domain.team.dto.response.CreateTeamResponse;
import indiv.abko.taskflow.domain.team.entity.Team;
import indiv.abko.taskflow.domain.team.exception.TeamErrorCode;
import indiv.abko.taskflow.domain.team.repository.TeamRepository;
import indiv.abko.taskflow.global.exception.BusinessException;

@ExtendWith(MockitoExtension.class)
public class CreateTeamUseCaseTest {
	@Mock
	private TeamRepository teamRepository;

	@InjectMocks
	private CreateTeamUseCase useCase;

	@Test
	public void 팀이_존재하지_않는_경우_팀을_생성할_수_있다() {
		// given
		String name = "개발팀";
		String description = "";

		Team team = new Team(name, description);
		ReflectionTestUtils.setField(team, "id", 1L);

		given(teamRepository.existsByName(anyString())).willReturn(false);
		given(teamRepository.save(any())).willReturn(team);

		// when
		CreateTeamResponse teamCreateResponse = useCase.execute(name, description);

		// then
		assertThat(teamCreateResponse).isNotNull();
		assertThat(teamCreateResponse.name()).isEqualTo(name);
	}

	@Test
	public void 팀이_존재하는_경우_BusinessException_에러를_던진다() {
		// given
		String name = "개발팀";
		String description = "";

		Team team = new Team(name, description);
		ReflectionTestUtils.setField(team, "id", 1L);

		given(teamRepository.existsByName(anyString())).willReturn(true);

		// when
		BusinessException exception = assertThrows(BusinessException.class,
			() -> useCase.execute(name, description));

		// then
		assertThat(exception.getErrorCode()).isEqualTo(TeamErrorCode.DUPLICATE_TEAM_NAME);
	}
}
