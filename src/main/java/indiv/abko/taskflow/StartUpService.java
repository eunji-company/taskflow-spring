package indiv.abko.taskflow;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import indiv.abko.taskflow.domain.user.entity.Member;
import indiv.abko.taskflow.domain.user.entity.UserRole;
import indiv.abko.taskflow.domain.user.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StartUpService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void runOnStartup() {
        Member member = Member.of("admin", passwordEncoder.encode("admin1234!"), "admin@admin.com", "관리자",
            UserRole.ADMIN);
        memberRepository.save(member);
    }
}
