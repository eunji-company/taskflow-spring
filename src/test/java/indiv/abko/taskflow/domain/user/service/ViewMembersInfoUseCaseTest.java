package indiv.abko.taskflow.domain.user.service;

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

import indiv.abko.taskflow.domain.user.dto.MembersInfoResponse;
import indiv.abko.taskflow.domain.user.entity.Member;
import indiv.abko.taskflow.domain.user.entity.UserRole;
import indiv.abko.taskflow.domain.user.repository.MemberRepository;

@ExtendWith(MockitoExtension.class)
public class ViewMembersInfoUseCaseTest {
	@Mock
	private MemberRepository memberRepository;

	@InjectMocks
	private ViewMembersInfoUseCase viewMembersInfoUseCase;

	@Test
	@DisplayName("멤버 목록을 정상적으로 조회한다")
	public void viewMembersInfoUseCase_성공() {
		//given
		Member member1 = Member.of("testusername", "HASHED_PW", "test@example.com", "testname", UserRole.USER);
		ReflectionTestUtils.setField(member1, "id", 1L);
		Member member2 = Member.of("testusername2", "HASHED_PW2", "test2@example.com", "testname2", UserRole.ADMIN);
		ReflectionTestUtils.setField(member2, "id", 2L);

		given(memberRepository.findAll()).willReturn(List.of(member1, member2));

		//when
		List<MembersInfoResponse> result = viewMembersInfoUseCase.execute();

		//then
		assertAll(() -> assertNotNull(result), () -> assertEquals(2, result.size()),
			() -> assertEquals(member1.getId(), result.get(0).id()),
			() -> assertEquals(member1.getEmail(), result.get(0).email()),
			() -> assertEquals(member1.getName(), result.get(0).name()),
			() -> assertEquals(member1.getUserRole().name(), result.get(0).role()),

			() -> assertEquals(member2.getId(), result.get(1).id()),
			() -> assertEquals(member2.getEmail(), result.get(1).email()),
			() -> assertEquals(member2.getName(), result.get(1).name()),
			() -> assertEquals(member2.getUserRole().name(), result.get(1).role()));
		verify(memberRepository).findAll();
	}

	@Test
	@DisplayName("멤버 목록을 조회할 때 멤버가 존재하지 않을 시 빈 목록을 반환한다.")
	public void viewMembersInfoUseCase_멤버가_존재하지_않으면_빈_목록을_반환한다() {
		//given
		given(memberRepository.findAll()).willReturn(List.of());

		//when
		List<MembersInfoResponse> result = viewMembersInfoUseCase.execute();

		//then
		assertAll(() -> assertNotNull(result), () -> assertTrue(result.isEmpty()));
		verify(memberRepository).findAll();
	}
}
