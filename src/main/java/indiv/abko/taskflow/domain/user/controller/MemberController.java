package indiv.abko.taskflow.domain.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import indiv.abko.taskflow.domain.user.dto.MemberInfoResponse;
import indiv.abko.taskflow.domain.user.service.ViewMemberInfoUseCase;
import indiv.abko.taskflow.global.auth.AuthMember;
import indiv.abko.taskflow.global.dto.CommonResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class MemberController {
	private final ViewMemberInfoUseCase viewMemberInfoUseCase;

	@GetMapping("/me")
	public ResponseEntity<CommonResponse<MemberInfoResponse>> getMemberInfo(
		@AuthenticationPrincipal AuthMember authMember) {
		return new ResponseEntity<>(
			CommonResponse.success("사용자 정보를 조회했습니다.", viewMemberInfoUseCase.execute(authMember.memberId())),
			HttpStatus.OK);
	}
}
