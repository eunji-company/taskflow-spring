package indiv.abko.taskflow.domain.comment.util;

import org.springframework.data.domain.Pageable;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PaginationContext {
    private final long startIdx;
    private final long endIdx;
    private final int size;
    private final int number;
    private long currentIdx = 0L;

    public PaginationContext(Pageable pageable) {
        startIdx = pageable.getOffset();
        endIdx = startIdx + pageable.getPageSize();
        size = pageable.getPageSize() == 0 ? 10 : pageable.getPageSize();
        number = pageable.getPageNumber();
    }

    public boolean isInRange() {
        return endIdx > currentIdx && currentIdx >= startIdx;
    }

    public boolean shouldStop() {
        return currentIdx >= endIdx;
    }

    public void next() {
        currentIdx++;
    }
}
