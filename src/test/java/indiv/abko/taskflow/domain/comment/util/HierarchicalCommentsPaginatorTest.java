package indiv.abko.taskflow.domain.comment.util;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.lenient;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import indiv.abko.taskflow.domain.comment.dto.CommentPaginationData;
import indiv.abko.taskflow.domain.comment.entity.Comment;
import indiv.abko.taskflow.domain.comment.util.HierarchicalCommentsPaginator.PagedComments;
import indiv.abko.taskflow.domain.task.entity.Task;
import indiv.abko.taskflow.domain.user.entity.Member;

@ExtendWith(MockitoExtension.class)
public class HierarchicalCommentsPaginatorTest {

    @Mock
    private Member mockMember;

    @Mock
    private Task mockTask;

    @Test
    @DisplayName("부모 댓글만 있을 때 정상적으로 페이지네이션한다")
    void paginate_부모댓글만_있을때_성공() {
        // given
        Comment parent1 = createParentComment(1L, "부모 댓글 1");
        Comment parent2 = createParentComment(2L, "부모 댓글 2");
        Comment parent3 = createParentComment(3L, "부모 댓글 3");

        List<Comment> parentComments = List.of(parent1, parent2, parent3);
        List<Comment> childComments = List.of();

        CommentPaginationData commentPaginationData = new CommentPaginationData(parentComments, childComments);
        HierarchicalCommentsPaginator paginator = new HierarchicalCommentsPaginator(commentPaginationData);

        Pageable pageable = PageRequest.of(0, 2);
        PaginationContext paginationContext = new PaginationContext(pageable);

        // when
        PagedComments result = paginator.paginate(paginationContext);

        // then
        assertAll(
            () -> assertEquals(2, result.comments().size()),
            () -> assertEquals(parent1, result.comments().get(0)),
            () -> assertEquals(parent2, result.comments().get(1)),
            () -> assertEquals(3, result.totalElements()),
            () -> assertEquals(2, result.totalPages()),
            () -> assertEquals(2, result.size()),
            () -> assertEquals(0, result.number()));
    }

    @Test
    @DisplayName("부모 댓글과 자식 댓글이 있을 때 정상적으로 페이지네이션한다")
    void paginate_부모댓글과_자식댓글이_있을때_성공() {
        // given
        Comment parent1 = createParentComment(1L, "부모 댓글 1");
        Comment parent2 = createParentComment(2L, "부모 댓글 2");

        Comment child1 = createChildComment(3L, "자식 댓글 1", parent1);
        Comment child2 = createChildComment(4L, "자식 댓글 2", parent1);
        Comment child3 = createChildComment(5L, "자식 댓글 3", parent2);

        List<Comment> parentComments = List.of(parent1, parent2);
        List<Comment> childComments = List.of(child1, child2, child3);

        CommentPaginationData commentPaginationData = new CommentPaginationData(parentComments, childComments);
        HierarchicalCommentsPaginator paginator = new HierarchicalCommentsPaginator(commentPaginationData);

        Pageable pageable = PageRequest.of(0, 4);
        PaginationContext paginationContext = new PaginationContext(pageable);

        // when
        PagedComments result = paginator.paginate(paginationContext);

        // then
        assertAll(
            () -> assertEquals(4, result.comments().size()), // 페이지 크기 4에 맞춰 정확히 제한됨
            () -> assertEquals(parent1, result.comments().get(0)),
            () -> assertEquals(child1, result.comments().get(1)),
            () -> assertEquals(child2, result.comments().get(2)),
            () -> assertEquals(parent2, result.comments().get(3)), // parent2까지만 포함 (child3 제외)
            () -> assertEquals(5, result.totalElements()),
            () -> assertEquals(2, result.totalPages()), // Math.ceil(5.0 / 4.0) = 2
            () -> assertEquals(4, result.size()),
            () -> assertEquals(0, result.number()));
    }

    @Test
    @DisplayName("페이지 크기에 맞춰 정확히 제한하여 페이지네이션한다")
    void paginate_페이지크기_제한_성공() {
        // given
        Comment parent1 = createParentComment(1L, "부모 댓글 1");
        Comment parent2 = createParentComment(2L, "부모 댓글 2");

        Comment child1 = createChildComment(3L, "자식 댓글 1", parent1);
        Comment child2 = createChildComment(4L, "자식 댓글 2", parent2);

        List<Comment> parentComments = List.of(parent1, parent2);
        List<Comment> childComments = List.of(child1, child2);

        CommentPaginationData commentPaginationData = new CommentPaginationData(parentComments, childComments);
        HierarchicalCommentsPaginator paginator = new HierarchicalCommentsPaginator(commentPaginationData);

        Pageable pageable = PageRequest.of(0, 3);
        PaginationContext paginationContext = new PaginationContext(pageable);

        // when
        PagedComments result = paginator.paginate(paginationContext);

        // then
        assertAll(
            () -> assertEquals(3, result.comments().size()), // 페이지 크기 3에 맞춰 정확히 제한됨
            () -> assertEquals(parent1, result.comments().get(0)),
            () -> assertEquals(child1, result.comments().get(1)),
            () -> assertEquals(parent2, result.comments().get(2)), // parent2까지만 포함 (child2 제외)
            () -> assertEquals(4, result.totalElements()),
            () -> assertEquals(2, result.totalPages()), // Math.ceil(4.0 / 3.0) = 2
            () -> assertEquals(3, result.size()),
            () -> assertEquals(0, result.number()));
    }

    @Test
    @DisplayName("두 번째 페이지를 정상적으로 조회한다")
    void paginate_두번째페이지_성공() {
        // given
        Comment parent1 = createParentComment(1L, "부모 댓글 1");
        Comment parent2 = createParentComment(2L, "부모 댓글 2");
        Comment parent3 = createParentComment(3L, "부모 댓글 3");

        List<Comment> parentComments = List.of(parent1, parent2, parent3);
        List<Comment> childComments = List.of();

        CommentPaginationData commentPaginationData = new CommentPaginationData(parentComments, childComments);
        HierarchicalCommentsPaginator paginator = new HierarchicalCommentsPaginator(commentPaginationData);

        Pageable pageable = PageRequest.of(1, 2);
        PaginationContext paginationContext = new PaginationContext(pageable);

        // when
        PagedComments result = paginator.paginate(paginationContext);

        // then
        assertAll(
            () -> assertEquals(1, result.comments().size()),
            () -> assertEquals(parent3, result.comments().get(0)),
            () -> assertEquals(3, result.totalElements()),
            () -> assertEquals(2, result.totalPages()),
            () -> assertEquals(1, result.size()),
            () -> assertEquals(1, result.number()));
    }

    @Test
    @DisplayName("빈 데이터일 때 빈 결과를 반환한다")
    void paginate_빈데이터_성공() {
        // given
        List<Comment> parentComments = List.of();
        List<Comment> childComments = List.of();

        CommentPaginationData commentPaginationData = new CommentPaginationData(parentComments, childComments);
        HierarchicalCommentsPaginator paginator = new HierarchicalCommentsPaginator(commentPaginationData);

        Pageable pageable = PageRequest.of(0, 10);
        PaginationContext paginationContext = new PaginationContext(pageable);

        // when
        PagedComments result = paginator.paginate(paginationContext);

        // then
        assertAll(
            () -> assertTrue(result.comments().isEmpty()),
            () -> assertEquals(0, result.totalElements()),
            () -> assertEquals(0, result.totalPages()),
            () -> assertEquals(0, result.size()),
            () -> assertEquals(0, result.number()));
    }

    @Test
    @DisplayName("자식 댓글이 없는 부모 댓글이 있을 때 정상적으로 처리한다")
    void paginate_자식댓글없는_부모댓글_성공() {
        // given
        Comment parent1 = createParentComment(1L, "부모 댓글 1");
        Comment parent2 = createParentComment(2L, "부모 댓글 2");

        Comment child1 = createChildComment(3L, "자식 댓글 1", parent1);
        // parent2는 자식 댓글이 없음

        List<Comment> parentComments = List.of(parent1, parent2);
        List<Comment> childComments = List.of(child1);

        CommentPaginationData commentPaginationData = new CommentPaginationData(parentComments, childComments);
        HierarchicalCommentsPaginator paginator = new HierarchicalCommentsPaginator(commentPaginationData);

        Pageable pageable = PageRequest.of(0, 10);
        PaginationContext paginationContext = new PaginationContext(pageable);

        // when
        PagedComments result = paginator.paginate(paginationContext);

        // then
        assertAll(
            () -> assertEquals(3, result.comments().size()),
            () -> assertEquals(parent1, result.comments().get(0)),
            () -> assertEquals(child1, result.comments().get(1)),
            () -> assertEquals(parent2, result.comments().get(2)),
            () -> assertEquals(3, result.totalElements()),
            () -> assertEquals(1, result.totalPages()),
            () -> assertEquals(3, result.size()),
            () -> assertEquals(0, result.number()));
    }

    private Comment createParentComment(Long id, String content) {
        Comment comment = mock(Comment.class);
        // getId()는 자식 댓글이 있는 경우에만 호출되므로 lenient로 설정
        lenient().when(comment.getId()).thenReturn(id);
        return comment;
    }

    private Comment createChildComment(Long id, String content, Comment parent) {
        Comment comment = mock(Comment.class);
        given(comment.getParentComment()).willReturn(parent);
        return comment;
    }
}
