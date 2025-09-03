package indiv.abko.taskflow.domain.user.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
	public void viewMemberInfoUserCase_성공() {
		//given
		Long memberId = 1L;
		Member member = Member.createForTest("testusername", "HASHED_PW", "test@example.com", "testname",
			UserRole.USER);
		given(memberRepository.findByIdOrElseThrow(memberId)).willReturn(member);

		//when
		MemberInfoResponse result = viewMemberInfoUseCase.execute(memberId);

		//then
		assertAll(() -> assertNotNull(result), () -> assertEquals(member.getUsername(), result.getUsername()),
			() -> assertEquals(member.getEmail(), result.getEmail()),
			() -> assertEquals(member.getName(), result.getName()),
			() -> assertEquals(member.getUserRole().name(), result.getRole()));
		verify(memberRepository).findByIdOrElseThrow(memberId);
	}

	@Test
	@DisplayName("멤버 정보를 조회할 때 멤버가 존재하지 않을 시 에러를 반환한다.")
	public void viewMemberInfoUserCase_멤버가_존재하지_않으면_에러를_던진다() {
		//given
		Long memberId = 1L;
		given(memberRepository.findByIdOrElseThrow(memberId)).willThrow(
			new BusinessException(MemberErrorCode.MEMBER_NOT_FOUND));

		//when
		BusinessException exception = assertThrows(BusinessException.class,
			() -> viewMemberInfoUseCase.execute(memberId));

		//then
		assertAll(() -> assertEquals(exception.getMessage(), MemberErrorCode.MEMBER_NOT_FOUND.getMessage()),
			() -> assertEquals(exception.getErrorCode(), MemberErrorCode.MEMBER_NOT_FOUND)
		);
		verify(memberRepository).findByIdOrElseThrow(memberId);
	}
}
