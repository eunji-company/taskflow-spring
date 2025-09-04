package indiv.abko.taskflow.team.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import indiv.abko.taskflow.domain.team.entity.Team;
import indiv.abko.taskflow.domain.team.exception.TeamErrorCode;
import indiv.abko.taskflow.domain.team.repository.TeamRepository;
import indiv.abko.taskflow.domain.team.service.DeleteTeamUseCase;
import indiv.abko.taskflow.global.exception.BusinessException;

@ExtendWith(MockitoExtension.class)
public class DeleteTeamUseCaseTest {
	@Mock
	private TeamRepository teamRepository;

	@InjectMocks
	private DeleteTeamUseCase useCase;

	@Test
	public void 팀이_존재하는_경우_팀을_제거할_수_있다() {
		// given
		Team team = new Team("개발팀", "팀이 생성되었습니다.");
		ReflectionTestUtils.setField(team, "id", 1L);

		given(teamRepository.findById(anyLong())).willReturn(Optional.of(team));
		doNothing().when(teamRepository).delete(team);

		// when
		useCase.execute(1L);

		// then
		verify(teamRepository, times(1)).delete(team);
	}

	@Test
	public void 팀이_존재하지_않는_경우_BusinessException_에러를_던진다() {
		// given
		given(teamRepository.findById(anyLong())).willReturn(Optional.empty());

		// when
		BusinessException exception = assertThrows(BusinessException.class,
			() -> useCase.execute(anyLong()));

		// then
		verify(teamRepository, never()).delete(any());
		assertThat(exception.getErrorCode()).isEqualTo(TeamErrorCode.NOT_FOUND_TEAM);
	}
}
