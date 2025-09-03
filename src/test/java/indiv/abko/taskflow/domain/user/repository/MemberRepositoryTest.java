package indiv.abko.taskflow.domain.user.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import indiv.abko.taskflow.domain.user.entity.Member;
import indiv.abko.taskflow.domain.user.entity.UserRole;
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
		Member member = Member.of("testusername", "HASHED_PW", "test@example.com", "testname",
			UserRole.USER);
		Member savedMember = memberRepository.save(member);
		memberRepository.flush();
		em.clear();

		//when
		Optional<Member> result = memberRepository.findById(savedMember.getId());

		//then
		assertTrue(result.isPresent());
		Member foundMember = result.get();
		assertAll(() -> assertNotNull(foundMember), () -> assertEquals(savedMember.getId(), foundMember.getId()),
			() -> assertEquals(savedMember.getUsername(), foundMember.getUsername()),
			() -> assertEquals(savedMember.getPassword(), foundMember.getPassword()),
			() -> assertEquals(savedMember.getEmail(), foundMember.getEmail()),
			() -> assertEquals(savedMember.getName(), foundMember.getName()),
			() -> assertEquals(savedMember.getUserRole(), foundMember.getUserRole()),
			() -> assertNull(foundMember.getDeletedAt()));
	}

	@Test
	@DisplayName("ID로 사용자를 조회할 때 사용자가 존재하지 않으면 Optional.empty를 반환한다.")
	void findById_멤버가_존재하지_않으면_empty() {
		//given
		Long notExistingMemberId = 999L;

		//when
		Optional<Member> result = memberRepository.findById(notExistingMemberId);

		//then
		assertTrue(result.isEmpty());
	}
}
