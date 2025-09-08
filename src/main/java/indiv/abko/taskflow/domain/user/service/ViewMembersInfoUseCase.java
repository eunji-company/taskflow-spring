package indiv.abko.taskflow.domain.user.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import indiv.abko.taskflow.domain.user.dto.MembersInfoResponse;
import indiv.abko.taskflow.domain.user.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ViewMembersInfoUseCase {
	private final MemberRepository memberRepository;

	@Transactional(readOnly = true)
	public List<MembersInfoResponse> execute() {
		return memberRepository.findAll()
			.stream()
			.map(member -> new MembersInfoResponse(member.getId(), member.getEmail(),
				member.getName(), member.getUserRole().name()))
			.toList();
	}
}
