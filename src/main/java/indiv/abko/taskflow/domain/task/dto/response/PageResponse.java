package indiv.abko.taskflow.domain.task.dto.response;

import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
public class PageResponse<T> {

	private final List<T> content;
	private final long totalElements;
	private final int totalPages;
	private final int size;
	private final int number;

	public PageResponse(Page<T> page) {
		this.content = page.getContent();
		this.totalElements = page.getTotalElements();
		this.totalPages = page.getTotalPages();
		this.size = page.getSize();
		this.number = page.getNumber();
	}
}
