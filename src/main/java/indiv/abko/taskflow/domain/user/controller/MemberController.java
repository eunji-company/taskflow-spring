package indiv.abko.taskflow.domain.user.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import indiv.abko.taskflow.domain.user.dto.MemberInfoResponse;
import indiv.abko.taskflow.domain.user.dto.MembersInfoResponse;
import indiv.abko.taskflow.domain.user.service.ViewAvailableMembersInfoUseCase;
import indiv.abko.taskflow.domain.user.service.ViewMemberInfoUseCase;
import indiv.abko.taskflow.domain.user.service.ViewMembersInfoUseCase;
import indiv.abko.taskflow.global.auth.AuthMember;
import indiv.abko.taskflow.global.dto.CommonResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class MemberController {
	private final ViewMemberInfoUseCase viewMemberInfoUseCase;
	private final ViewMembersInfoUseCase viewMembersInfoUseCase;
	private final ViewAvailableMembersInfoUseCase viewAvailableMembersInfoUseCase;

	@GetMapping("/me")
	public CommonResponse<MemberInfoResponse> getMember(@AuthenticationPrincipal AuthMember authMember) {
		return CommonResponse.success("사용자 정보를 조회했습니다.", viewMemberInfoUseCase.execute(authMember.memberId()));
	}

	@GetMapping("/")
	public CommonResponse<List<MembersInfoResponse>> getAllMembers() {
		return CommonResponse.success("요청이 성공적으로 처리되었습니다.", viewMembersInfoUseCase.execute());
	}

	@GetMapping("/available")
	public CommonResponse<List<MemberInfoResponse>> getAvailableMembers(
		@RequestParam(required = true, name = "teamId") Long teamId) {
		return CommonResponse.success("사용 가능한 사용자 목록을 조회했습니다.",
			viewAvailableMembersInfoUseCase.execute(teamId));
	}

}
