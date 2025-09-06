package indiv.abko.taskflow.domain.user.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import indiv.abko.taskflow.domain.team.entity.Team;
import indiv.abko.taskflow.domain.team.exception.TeamErrorCode;
import indiv.abko.taskflow.domain.team.service.TeamServiceApi;
import indiv.abko.taskflow.domain.user.dto.MemberInfoResponse;
import indiv.abko.taskflow.domain.user.entity.Member;
import indiv.abko.taskflow.domain.user.entity.UserRole;
import indiv.abko.taskflow.domain.user.repository.MemberRepository;
import indiv.abko.taskflow.global.exception.BusinessException;

@ExtendWith(MockitoExtension.class)
public class ViewAvailableMembersInfoUseCaseTest {
	@Mock
	private MemberRepository memberRepository;

	@Mock
	private TeamServiceApi teamService;

	@InjectMocks
	private ViewAvailableMembersInfoUseCase viewAvailableMembersInfoUseCase;

	@Test
	@DisplayName("팀이 존재할 때 available 멤버 목록을 정상적으로 조회한다")
	public void viewAvailableMembersInfoUseCase_성공() {
		//given
		Long teamId = 1L;

		Team team = new Team("teamName", "teamDescription");
		given(teamService.getByIdOrThrow(teamId)).willReturn(team);

		Member member2 = Member.of("testusername2", "HASHED_PW2", "test2@example.com", "testname2", UserRole.ADMIN);
		ReflectionTestUtils.setField(member2, "id", 2L);

		Member member3 = Member.of("testusername3", "HASHED_PW3", "test3@example.com", "testname3", UserRole.USER);
		ReflectionTestUtils.setField(member3, "id", 3L);

		List<Member> expected = List.of(member2, member3);
		given(memberRepository.findAvailableMembersForTeam(team)).willReturn(expected);

		//when
		List<MemberInfoResponse> result = viewAvailableMembersInfoUseCase.execute(teamId);

		//then
		assertThat(result).hasSize(2);
		assertThat(result)
			.extracting(
				MemberInfoResponse::id,
				MemberInfoResponse::username,
				MemberInfoResponse::email,
				MemberInfoResponse::name,
				MemberInfoResponse::role
			)
			.containsExactly(
				tuple(2L, "testusername2", "test2@example.com", "testname2", "ADMIN"),
				tuple(3L, "testusername3", "test3@example.com", "testname3", "USER")
			);
		verify(teamService, times(1)).getByIdOrThrow(teamId);
		verify(memberRepository, times(1)).findAvailableMembersForTeam(team);
	}

	@Test
	@DisplayName("팀이 존재하지 않을 시 NOT_FOUND_TEAM 오류를 반환한다.")
	public void viewAvailableMembersInfoUseCase_팀이_존재하지_않으면_NOT_FOUND_TEAM() {
		//given
		Long teamId = 1L;
		doThrow(new BusinessException(TeamErrorCode.NOT_FOUND_TEAM))
			.when(teamService).getByIdOrThrow(teamId);

		//when
		BusinessException exception = assertThrows(BusinessException.class,
			() -> viewAvailableMembersInfoUseCase.execute(teamId));

		//then
		assertAll(() -> assertEquals(TeamErrorCode.NOT_FOUND_TEAM.getMessage(), exception.getMessage()),
			() -> assertEquals(TeamErrorCode.NOT_FOUND_TEAM, exception.getErrorCode()));

		verify(teamService, times(1)).getByIdOrThrow(teamId);
		verifyNoMoreInteractions(teamService);
	}
}
