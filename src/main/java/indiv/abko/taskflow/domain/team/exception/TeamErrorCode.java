package indiv.abko.taskflow.domain.team.exception;

import org.springframework.http.HttpStatus;

import indiv.abko.taskflow.global.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TeamErrorCode implements ErrorCode {
	DUPLICATE_TEAM_NAME(HttpStatus.BAD_REQUEST, "팀 이름이 이미 존재합니다"),
	NOT_FOUND_TEAM(HttpStatus.NOT_FOUND, "팀이 존재하지 않습니다"),
	NOT_TEAM_MEMBER(HttpStatus.BAD_REQUEST, "사용자가 팀 멤버가 아닙니다"),
	TEAM_MEMBER_ALREADY_EXISTS(HttpStatus.CONFLICT, "사용자가 이미 팀 멤버입니다");

	private final HttpStatus httpStatus;
	private final String message;
}
