package indiv.abko.taskflow.domain.team.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
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
import indiv.abko.taskflow.domain.user.entity.Member;
import indiv.abko.taskflow.domain.user.entity.UserRole;
import indiv.abko.taskflow.global.exception.BusinessException;

@ExtendWith(MockitoExtension.class)
public class UpdateTeamUseCaseTest {
	@Mock
	private TeamRepository teamRepository;

	@InjectMocks
	private UpdateTeamUseCase useCase;

	@Test
	public void 팀이_존재하는_경우_팀을_수정할_수_있다() {
		// given
		String name = "프론트엔드팀";
		String description = "프론트엔드 전문 개발팀";

		Team team = new Team("개발팀", "팀이 생성되었습니다.");
		Member member = Member.of("testUser", "12345678", "test@example.com", "홍길동", UserRole.USER);
		ReflectionTestUtils.setField(member, "id", 1L);
		team.addMember(member);
		ReflectionTestUtils.setField(team, "id", 1L);

		given(teamRepository.findWithTeamMembersById(anyLong())).willReturn(Optional.of(team));

		// when
		useCase.execute(name, description, 1L);

		// then
		assertThat(team.getName()).isEqualTo(name);
		assertThat(team.getDescription()).isEqualTo(description);
	}

	@Test
	public void 팀이_존재하지_않는_경우_BusinessException_에러를_던진다() {
		// given
		given(teamRepository.findWithTeamMembersById(anyLong())).willReturn(Optional.empty());

		// when
		BusinessException exception = assertThrows(BusinessException.class,
			() -> useCase.execute("프론트엔트팀", "프론트엔드 전문 개발팀", 1L));

		// then
		assertThat(exception.getErrorCode()).isEqualTo(TeamErrorCode.NOT_FOUND_TEAM);
	}
}
