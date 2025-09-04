package indiv.abko.taskflow.domain.team.service;

import java.util.List;

import indiv.abko.taskflow.domain.user.dto.MemberInfoResponse;

public interface TeamServiceApi {
	// TODO: 팀이 존재하지 않으면 "팀을 찾을 수 없습니다"라는 메시지로 404 오류 반환
	default void existsById(Long teamId) {

	}

	// TODO: 멤버가 팀에 속하지 않으면 "사용자가 팀 멤버가 아닙니다"라는 메시지로 400 오류 반환
	default void existsMemberInTeam(Long teamId, Long memberId) {

	}

	// TODO: 해당 팀에 속하지 않고, 삭제되지 않은 모든 사용자 목록 반환
	default List<MemberInfoResponse> getAvailableMembersForTeam(Long teamId) {
		// MemberInfoResponse 사용 예시
		// new MemberInfoResponse(member.getId(), member.getUsername(), member.getEmail(), member.getName(), member.getUserRole().name(), member.getCreatedAt())
		return null;
	}
}
