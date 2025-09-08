package indiv.abko.taskflow.domain.comment.service;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import indiv.abko.taskflow.domain.comment.dto.CommentPaginationData;
import indiv.abko.taskflow.domain.comment.dto.command.ViewCommentsFromTaskCommand;
import indiv.abko.taskflow.domain.comment.dto.response.ViewCommentsFromTaskResponse;
import indiv.abko.taskflow.domain.comment.entity.Comment;
import indiv.abko.taskflow.domain.comment.enums.CommentSortOption;
import indiv.abko.taskflow.domain.comment.mapper.CommentMapper;
import indiv.abko.taskflow.domain.comment.repository.CommentRepository;
import indiv.abko.taskflow.domain.comment.util.HierarchicalCommentsPaginator;
import indiv.abko.taskflow.domain.comment.util.HierarchicalCommentsPaginatorFactory;
import indiv.abko.taskflow.domain.comment.util.HierarchicalCommentsPaginator.PagedComments;

@ExtendWith(MockitoExtension.class)
public class ViewCommentsFromTaskUseCaseTest {
    @Mock
    private CommentRepository commentRepository;

    @Mock
    private CommentMapper commentMapper;

    @Mock
    private HierarchicalCommentsPaginatorFactory paginatorFactory;

    @Mock
    private HierarchicalCommentsPaginator mockPaginator;

    @InjectMocks
    private ViewCommentsFromTaskUseCase viewCommentsFromTaskUseCase;

    @Test
    void 성공적으로_task의_댓글을_계층적으로_조회한다() {
        // given
        long taskId = 1L;
        Pageable pageable = PageRequest.of(0, 10);
        ViewCommentsFromTaskCommand command = new ViewCommentsFromTaskCommand(pageable, taskId,
            CommentSortOption.NEWEST);

        List<Comment> parentComments = List.of();
        List<Comment> childComments = List.of();
        PagedComments pagedComments = new PagedComments(List.of(), 0L, 0, 0, 0);
        ViewCommentsFromTaskResponse expectedResponse = ViewCommentsFromTaskResponse.builder()
            .content(List.of())
            .totalElements(0L)
            .totalPages(0)
            .size(0)
            .number(0)
            .build();

        given(commentRepository.findWithDetailsByTaskIdAndParentCommentIsNullOrderByCreatedAtDesc(taskId))
            .willReturn(parentComments);
        given(commentRepository.findWithDetailByParentCommentIsInOrderByCreatedAt(parentComments))
            .willReturn(childComments);
        given(paginatorFactory.create(any(CommentPaginationData.class))).willReturn(mockPaginator);
        given(mockPaginator.paginate(any())).willReturn(pagedComments);
        given(commentMapper.toViewCommentsFromTaskResponse(pagedComments)).willReturn(expectedResponse);

        // when
        ViewCommentsFromTaskResponse result = viewCommentsFromTaskUseCase.execute(command);

        // then
        assertNotNull(result);
        then(paginatorFactory).should(times(1)).create(any(CommentPaginationData.class));
        then(mockPaginator).should(times(1)).paginate(any());
        then(commentMapper).should(times(1)).toViewCommentsFromTaskResponse(pagedComments);
    }

    @Test
    void OLDEST_정렬옵션으로_댓글을_조회한다() {
        // given
        long taskId = 1L;
        Pageable pageable = PageRequest.of(0, 10);
        ViewCommentsFromTaskCommand command = new ViewCommentsFromTaskCommand(pageable, taskId,
            CommentSortOption.OLDEST);

        List<Comment> parentComments = List.of();
        List<Comment> childComments = List.of();
        PagedComments pagedComments = new PagedComments(List.of(), 0L, 0, 0, 0);
        ViewCommentsFromTaskResponse expectedResponse = ViewCommentsFromTaskResponse.builder()
            .content(List.of())
            .totalElements(0L)
            .totalPages(0)
            .size(0)
            .number(0)
            .build();

        given(commentRepository.findWithDetailsByTaskIdAndParentCommentIsNullOrderByCreatedAt(taskId))
            .willReturn(parentComments);
        given(commentRepository.findWithDetailByParentCommentIsInOrderByCreatedAt(parentComments))
            .willReturn(childComments);
        given(paginatorFactory.create(any(CommentPaginationData.class))).willReturn(mockPaginator);
        given(mockPaginator.paginate(any())).willReturn(pagedComments);
        given(commentMapper.toViewCommentsFromTaskResponse(pagedComments)).willReturn(expectedResponse);

        // when
        ViewCommentsFromTaskResponse result = viewCommentsFromTaskUseCase.execute(command);

        // then
        assertNotNull(result);
        then(commentRepository).should(times(1))
            .findWithDetailsByTaskIdAndParentCommentIsNullOrderByCreatedAt(taskId);
    }
}
