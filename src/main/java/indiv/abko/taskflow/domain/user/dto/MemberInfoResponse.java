package indiv.abko.taskflow.domain.user.dto;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonFormat;

import indiv.abko.taskflow.global.dto.DtoConstants;

public record MemberInfoResponse(Long id, String username, String email, String name, String role,
								 @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DtoConstants.TIME_FORMAT, timezone = DtoConstants.DISPLAY_TIME_ZONE_STRING) Instant createdAt) {
}
