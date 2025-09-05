package indiv.abko.taskflow.domain.user.repository;

import static org.assertj.core.api.AssertionsForInterfaceTypes.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.util.ReflectionTestUtils;

import indiv.abko.taskflow.domain.team.entity.Team;
import indiv.abko.taskflow.domain.user.entity.Member;
import indiv.abko.taskflow.domain.user.entity.UserRole;
import indiv.abko.taskflow.global.config.JpaAuditingConfig;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@DataJpaTest
@Import(JpaAuditingConfig.class)
public class MemberRepositoryTest {
	@Autowired
	private MemberRepository memberRepository;

	@PersistenceContext
	private EntityManager em;

	@Test
	@DisplayName("ID로 사용자를 정상적으로 조회한다")
	void findById_성공() {
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

	@Test
	@DisplayName("삭제되지 않은 모든 사용자를 정상적으로 조회한다")
	void findAll_성공() {
		//given
		Member member1 = Member.of("testusername", "HASHED_PW", "test@example.com", "testname", UserRole.USER);
		Member member2 = Member.of("testusername2", "HASHED_PW2", "test2@example.com", "testname2", UserRole.ADMIN);
		Member deletedMember3 = Member.of("testusername3", "HASHED_PW3", "test3@example.com", "testname3",
			UserRole.USER);
		ReflectionTestUtils.setField(deletedMember3, "deletedAt", LocalDateTime.now());
		memberRepository.save(member1);
		memberRepository.save(member2);
		memberRepository.save(deletedMember3);
		memberRepository.flush();
		em.clear();

		//when
		List<Member> result = memberRepository.findAll();

		//then
		assertNotNull(result);
		assertEquals(2, result.size());
		Member foundMember1 = result.get(0);
		Member foundMember2 = result.get(1);

		assertAll(
			() -> assertNotNull(foundMember1.getId()),
			() -> assertEquals(member1.getUsername(), foundMember1.getUsername()),
			() -> assertEquals(member1.getPassword(), foundMember1.getPassword()),
			() -> assertEquals(member1.getEmail(), foundMember1.getEmail()),
			() -> assertEquals(member1.getName(), foundMember1.getName()),
			() -> assertEquals(member1.getUserRole(), foundMember1.getUserRole()),
			() -> assertNull(foundMember1.getDeletedAt()),
			() -> assertNotNull(foundMember1.getCreatedAt()),
			() -> assertNotNull(foundMember1.getModifiedAt()),

			() -> assertNotNull(foundMember2.getId()),
			() -> assertEquals(member2.getUsername(), foundMember2.getUsername()),
			() -> assertEquals(member2.getPassword(), foundMember2.getPassword()),
			() -> assertEquals(member2.getEmail(), foundMember2.getEmail()),
			() -> assertEquals(member2.getName(), foundMember2.getName()),
			() -> assertEquals(member2.getUserRole(), foundMember2.getUserRole()),
			() -> assertNull(foundMember2.getDeletedAt()),
			() -> assertNotNull(foundMember2.getCreatedAt()),
			() -> assertNotNull(foundMember2.getModifiedAt())
		);
	}

	@Test
	@DisplayName("삭제되지 않은 모든 사용자를 조회할 때 사용자가 존재하지 않으면 empty list를 반환한다.")
	void findAll_멤버가_존재하지_않으면_empty() {
		//given

		//when
		List<Member> result = memberRepository.findAll();

		//then
		assertTrue(result.isEmpty());
	}

	@Test
	@DisplayName("해당 팀에 속하지 않고 삭제되지 않은 모든 사용자를 조회한다")
	void findAvailableMembersForTeam_성공() {
		//given
		Team team1 = new Team("Team1", "Description1");
		em.persist(team1);

		Member memberInTeam = Member.of("testusername", "HASHED_PW", "test@example.com", "testname", UserRole.USER);
		Member member1 = Member.of("testusername1", "HASHED_PW1", "test1@example.com", "testname1", UserRole.USER);
		Member member2 = Member.of("testusername2", "HASHED_PW2", "test2@example.com", "testname2", UserRole.ADMIN);
		Member deletedMember3 = Member.of("testusername3", "HASHED_PW3", "test3@example.com", "testname3",
			UserRole.USER);
		ReflectionTestUtils.setField(deletedMember3, "deletedAt", LocalDateTime.now());
		memberRepository.save(memberInTeam);
		memberRepository.save(member1);
		memberRepository.save(member2);
		memberRepository.save(deletedMember3);

		team1.addMember(memberInTeam);

		memberRepository.flush();
		em.clear();

		//when
		List<Member> result = memberRepository.findAvailableMembersForTeam(team1);

		//then
		assertNotNull(result);
		assertEquals(2, result.size());

		assertThat(result)
			.extracting(
				Member::getUsername,
				Member::getEmail,
				Member::getName,
				Member::getUserRole
			)
			.containsExactlyInAnyOrder(
				tuple("testusername1", "test1@example.com", "testname1", UserRole.USER),
				tuple("testusername2", "test2@example.com", "testname2", UserRole.ADMIN)
			);
	}

	@Test
	@DisplayName("모든 사용자가 팀에 속해있을 경우 빈 목록을 조회한다")
	void findAvailableMembersForTeam_모든_사용자가_팀에_속할경우_empty_list() {
		//given
		Team team1 = new Team("Team1", "Description1");
		em.persist(team1);

		Member memberInTeam = Member.of("testusername", "HASHED_PW", "test@example.com", "testname", UserRole.USER);
		Member memberInTeam2 = Member.of("testusername2", "HASHED_PW2", "test2@example.com", "testname2",
			UserRole.USER);
		memberRepository.save(memberInTeam);
		memberRepository.save(memberInTeam2);

		team1.addMember(memberInTeam);
		team1.addMember(memberInTeam2);

		memberRepository.flush();
		em.clear();

		//when
		List<Member> result = memberRepository.findAvailableMembersForTeam(team1);

		//then
		assertThat(result).isEmpty();
	}

}
