package indiv.abko.taskflow.team.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import indiv.abko.taskflow.domain.team.dto.response.ReadTeamResponse;
import indiv.abko.taskflow.domain.team.entity.Team;
import indiv.abko.taskflow.domain.team.repository.TeamMemberRepository;
import indiv.abko.taskflow.domain.team.service.ReadTeamUseCase;
import indiv.abko.taskflow.domain.user.entity.Member;
import indiv.abko.taskflow.domain.user.entity.UserRole;

@ExtendWith(MockitoExtension.class)
public class ReadTeamUseCaseTest {
	@Mock
	private TeamMemberRepository teamMemberRepository;

	@InjectMocks
	private ReadTeamUseCase readTeamUseCase;

	@Test
	void 팀_목록_조회를_성공한다() {
		// given
		Member member = Member.of("testUser", "12345678", "test@example.com", "홍길동", UserRole.USER);
		ReflectionTestUtils.setField(member, "id", 1L);

		// 테스트용 Team 생성
		Team team = new Team("Dev Team", "개발팀"); // ArrayList로 바꾸기 (가변)
		ReflectionTestUtils.setField(team, "id", 1L);
		team.addMember(member);

		when(teamMemberRepository.findAllWithFetchBy()).thenReturn(team.getTeamMembers());

		// when
		List<ReadTeamResponse> responses = readTeamUseCase.execute();

		// then
		assertThat(responses).hasSize(1);
		ReadTeamResponse response = responses.get(0);

		assertThat(response.name()).isEqualTo("Dev Team");
		assertThat(response.members()).hasSize(1);
		assertThat(response.members().get(0).username()).isEqualTo("testUser");
	}
}
