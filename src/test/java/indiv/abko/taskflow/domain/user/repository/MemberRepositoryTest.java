package indiv.abko.taskflow.domain.user.repository;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import indiv.abko.taskflow.domain.user.entity.Member;
import indiv.abko.taskflow.domain.user.entity.UserRole;
import indiv.abko.taskflow.domain.user.exception.MemberErrorCode;
import indiv.abko.taskflow.global.exception.BusinessException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@DataJpaTest
public class MemberRepositoryTest {
	@Autowired
	private MemberRepository memberRepository;

	@PersistenceContext
	private EntityManager em;

	@Test
	@DisplayName("ID로 사용자를 정상적으로 조회한다")
	void findByIdOrElseThrow_성공() {
		//given
		Member member = Member.createForTest("testusername", "HASHED_PW", "test@example.com", "testname",
			UserRole.USER);
		Member savedMember = memberRepository.save(member);
		memberRepository.flush();
		em.clear();

		//when
		Member foundMember = memberRepository.findByIdOrElseThrow(savedMember.getId());

		//then
		assertAll(() -> assertNotNull(foundMember), () -> assertEquals(savedMember.getId(), foundMember.getId()),
			() -> assertEquals(savedMember.getUsername(), foundMember.getUsername()),
			() -> assertEquals(savedMember.getPassword(), foundMember.getPassword()),
			() -> assertEquals(savedMember.getEmail(), foundMember.getEmail()),
			() -> assertEquals(savedMember.getName(), foundMember.getName()),
			() -> assertEquals(savedMember.getUserRole().name(), foundMember.getUserRole().name()),
			() -> assertNull(foundMember.getDeletedAt()));
	}

	@Test
	@DisplayName("ID로 사용자를 조회할 때 사용자가 존재하지 않으면 에러를 던진다")
	void findByIdOrElseThrow_멤버가_존재하지_않으면_에러를_던진다() {
		//given
		Long notExistingMemberId = 999L;

		//when
		BusinessException exception = assertThrows(BusinessException.class,
			() -> memberRepository.findByIdOrElseThrow(notExistingMemberId));

		//then
		assertAll(() -> assertEquals(exception.getMessage(), MemberErrorCode.MEMBER_NOT_FOUND.getMessage()),
			() -> assertEquals(exception.getErrorCode(), MemberErrorCode.MEMBER_NOT_FOUND)
		);
	}
}
