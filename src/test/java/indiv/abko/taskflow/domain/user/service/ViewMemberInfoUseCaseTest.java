package indiv.abko.taskflow.domain.user.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import indiv.abko.taskflow.domain.user.dto.MemberInfoResponse;
import indiv.abko.taskflow.domain.user.entity.Member;
import indiv.abko.taskflow.domain.user.entity.UserRole;
import indiv.abko.taskflow.domain.user.exception.MemberErrorCode;
import indiv.abko.taskflow.domain.user.repository.MemberRepository;
import indiv.abko.taskflow.global.exception.BusinessException;

@ExtendWith(MockitoExtension.class)
public class ViewMemberInfoUseCaseTest {
	@Mock
	private MemberRepository memberRepository;

	@InjectMocks
	private ViewMemberInfoUseCase viewMemberInfoUseCase;

	@Test
	@DisplayName("멤버 정보를 정상적으로 조회한다")
	public void viewMemberInfoUseCase_성공() {
		//given
		Long memberId = 1L;
		Member member = Member.of("testusername", "HASHED_PW", "test@example.com", "testname", UserRole.USER);
		ReflectionTestUtils.setField(member, "createdAt", LocalDateTime.now());
		given(memberRepository.findById(memberId)).willReturn(Optional.of(member));

		//when
		MemberInfoResponse result = viewMemberInfoUseCase.execute(memberId);

		//then
		assertAll(() -> assertNotNull(result), () -> assertEquals(member.getUsername(), result.username()),
			() -> assertEquals(member.getEmail(), result.email()), () -> assertEquals(member.getName(), result.name()),
			() -> assertEquals(member.getUserRole().name(), result.role()));
		verify(memberRepository).findById(memberId);
	}

	@Test
	@DisplayName("멤버 정보를 조회할 때 멤버가 존재하지 않을 시 에러를 반환한다.")
	public void viewMemberInfoUseCase_멤버가_존재하지_않으면_에러를_던진다() {
		//given
		Long memberId = 1L;
		given(memberRepository.findById(memberId)).willReturn(Optional.empty());

		//when
		BusinessException exception = assertThrows(BusinessException.class,
			() -> viewMemberInfoUseCase.execute(memberId));

		//then
		assertAll(() -> assertEquals(MemberErrorCode.MEMBER_NOT_FOUND.getMessage(), exception.getMessage()),
			() -> assertEquals(MemberErrorCode.MEMBER_NOT_FOUND, exception.getErrorCode()));
		verify(memberRepository).findById(memberId);
	}
}
