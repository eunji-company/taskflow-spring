package indiv.abko.taskflow.domain.comment.dto.response;

import java.time.Instant;
import java.util.List;

import lombok.Builder;

@Builder
public record ViewCommentsFromTaskResponse(
    List<CommentResp> content,
    long totalElements,
    int totalPages,
    int size,
    int number) {

    @Builder
    public record CommentResp(
        Long id,
        String content,
        Long taskId,
        Long userId,
        UserResp user,
        Long parentId,
        Instant createdAt,
        Instant updatedAt) {
    }

    @Builder
    public record UserResp(
        Long id,
        String username,
        String name,
        String email,
        String role) {
    }
}