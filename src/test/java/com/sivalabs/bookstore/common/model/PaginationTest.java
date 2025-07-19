package com.sivalabs.bookstore.common.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

class PaginationTest {

    @Test
    void shouldHandleNormalPage() {
        // Given
        List<String> items = List.of("Item 1", "Item 2", "Item 3");
        Page<String> page = new PageImpl<>(items, PageRequest.of(1, 3), 9);
        PagedResult<String> pagedResult = new PagedResult<>(page);
        Pagination<String> pagination = new Pagination<>(pagedResult, "/items");

        // Then
        assertThat(pagination.hasData()).isTrue();
        assertThat(pagination.currentPageNo()).isEqualTo(2);
        assertThat(pagination.totalPages()).isEqualTo(3);
        assertThat(pagination.totalElements()).isEqualTo(9);
        assertThat(pagination.isFirstPage()).isFalse();
        assertThat(pagination.isLastPage()).isFalse();
        assertThat(pagination.hasNextPage()).isTrue();
        assertThat(pagination.hasPreviousPage()).isTrue();

        assertThat(pagination.getFirstPageLink()).isEqualTo("/items?page=1");
        assertThat(pagination.getLastPageLink()).isEqualTo("/items?page=3");
        assertThat(pagination.getPreviousPageLink()).isEqualTo("/items?page=1");
        assertThat(pagination.getNextPageLink()).isEqualTo("/items?page=3");
    }

    @Test
    void shouldHandleFirstPage() {
        // Given
        List<String> items = List.of("Item 1", "Item 2", "Item 3");
        Page<String> page = new PageImpl<>(items, PageRequest.of(0, 3), 9);
        PagedResult<String> pagedResult = new PagedResult<>(page);
        Pagination<String> pagination = new Pagination<>(pagedResult, "/items");

        // Then
        assertThat(pagination.hasData()).isTrue();
        assertThat(pagination.currentPageNo()).isEqualTo(1);
        assertThat(pagination.totalPages()).isEqualTo(3);
        assertThat(pagination.totalElements()).isEqualTo(9);
        assertThat(pagination.isFirstPage()).isTrue();
        assertThat(pagination.isLastPage()).isFalse();
        assertThat(pagination.hasNextPage()).isTrue();
        assertThat(pagination.hasPreviousPage()).isFalse();

        assertThat(pagination.getFirstPageLink()).isEqualTo("#");
        assertThat(pagination.getLastPageLink()).isEqualTo("/items?page=3");
        assertThat(pagination.getPreviousPageLink()).isEqualTo("#");
        assertThat(pagination.getNextPageLink()).isEqualTo("/items?page=2");
    }

    @Test
    void shouldHandleLastPage() {
        // Given
        List<String> items = List.of("Item 7", "Item 8", "Item 9");
        Page<String> page = new PageImpl<>(items, PageRequest.of(2, 3), 9);
        PagedResult<String> pagedResult = new PagedResult<>(page);
        Pagination<String> pagination = new Pagination<>(pagedResult, "/items");

        // Then
        assertThat(pagination.hasData()).isTrue();
        assertThat(pagination.currentPageNo()).isEqualTo(3);
        assertThat(pagination.totalPages()).isEqualTo(3);
        assertThat(pagination.totalElements()).isEqualTo(9);
        assertThat(pagination.isFirstPage()).isFalse();
        assertThat(pagination.isLastPage()).isTrue();
        assertThat(pagination.hasNextPage()).isFalse();
        assertThat(pagination.hasPreviousPage()).isTrue();

        assertThat(pagination.getFirstPageLink()).isEqualTo("/items?page=1");
        assertThat(pagination.getLastPageLink()).isEqualTo("#");
        assertThat(pagination.getPreviousPageLink()).isEqualTo("/items?page=2");
        assertThat(pagination.getNextPageLink()).isEqualTo("#");
    }

    @Test
    void shouldHandleSinglePage() {
        // Given
        List<String> items = List.of("Item 1", "Item 2", "Item 3");
        Page<String> page = new PageImpl<>(items, PageRequest.of(0, 10), 3);
        PagedResult<String> pagedResult = new PagedResult<>(page);
        Pagination<String> pagination = new Pagination<>(pagedResult, "/items");

        // Then
        assertThat(pagination.hasData()).isTrue();
        assertThat(pagination.currentPageNo()).isEqualTo(1);
        assertThat(pagination.totalPages()).isEqualTo(1);
        assertThat(pagination.totalElements()).isEqualTo(3);
        assertThat(pagination.isFirstPage()).isTrue();
        assertThat(pagination.isLastPage()).isTrue();
        assertThat(pagination.hasNextPage()).isFalse();
        assertThat(pagination.hasPreviousPage()).isFalse();

        assertThat(pagination.getFirstPageLink()).isEqualTo("#");
        assertThat(pagination.getLastPageLink()).isEqualTo("#");
        assertThat(pagination.getPreviousPageLink()).isEqualTo("#");
        assertThat(pagination.getNextPageLink()).isEqualTo("#");
    }

    @Test
    void shouldHandleEmptyResults() {
        // Given
        List<String> items = Collections.emptyList();
        Page<String> page = new PageImpl<>(items, PageRequest.of(0, 10), 0);
        PagedResult<String> pagedResult = new PagedResult<>(page);
        Pagination<String> pagination = new Pagination<>(pagedResult, "/items");

        // Then
        assertThat(pagination.hasData()).isFalse();
        assertThat(pagination.currentPageNo()).isEqualTo(1);
        assertThat(pagination.totalPages()).isEqualTo(0);
        assertThat(pagination.totalElements()).isEqualTo(0);
        assertThat(pagination.isFirstPage()).isFalse();
        assertThat(pagination.isLastPage()).isFalse();
        assertThat(pagination.hasNextPage()).isFalse();
        assertThat(pagination.hasPreviousPage()).isFalse();

        assertThat(pagination.getFirstPageLink()).isEqualTo("#");
        assertThat(pagination.getLastPageLink()).isEqualTo("#");
        assertThat(pagination.getPreviousPageLink()).isEqualTo("#");
        assertThat(pagination.getNextPageLink()).isEqualTo("#");
    }

    @Test
    void shouldHandleAdditionalQueryParameters() {
        // Given
        List<String> items = List.of("Item 1", "Item 2", "Item 3");
        Page<String> page = new PageImpl<>(items, PageRequest.of(1, 3), 9);
        PagedResult<String> pagedResult = new PagedResult<>(page);
        Map<String, Object> queryParams = Map.of("category", "books", "sort", "title");
        Pagination<String> pagination = new Pagination<>(pagedResult, "/items", queryParams);

        // Then
        assertThat(pagination.getFirstPageLink()).contains("/items?");
        assertThat(pagination.getFirstPageLink()).contains("page=1");
        assertThat(pagination.getFirstPageLink()).contains("category=books");
        assertThat(pagination.getFirstPageLink()).contains("sort=title");

        assertThat(pagination.getLastPageLink()).contains("/items?");
        assertThat(pagination.getLastPageLink()).contains("page=3");
        assertThat(pagination.getLastPageLink()).contains("category=books");
        assertThat(pagination.getLastPageLink()).contains("sort=title");

        assertThat(pagination.getPreviousPageLink()).contains("/items?");
        assertThat(pagination.getPreviousPageLink()).contains("page=1");
        assertThat(pagination.getPreviousPageLink()).contains("category=books");
        assertThat(pagination.getPreviousPageLink()).contains("sort=title");

        assertThat(pagination.getNextPageLink()).contains("/items?");
        assertThat(pagination.getNextPageLink()).contains("page=3");
        assertThat(pagination.getNextPageLink()).contains("category=books");
        assertThat(pagination.getNextPageLink()).contains("sort=title");
    }

    @Test
    void shouldHandleEmptyQueryParameters() {
        // Given
        List<String> items = List.of("Item 1", "Item 2", "Item 3");
        Page<String> page = new PageImpl<>(items, PageRequest.of(1, 3), 9);
        PagedResult<String> pagedResult = new PagedResult<>(page);
        Map<String, Object> queryParams = Collections.emptyMap();
        Pagination<String> pagination = new Pagination<>(pagedResult, "/items", queryParams);

        // Then
        assertThat(pagination.getFirstPageLink()).isEqualTo("/items?page=1");
        assertThat(pagination.getLastPageLink()).isEqualTo("/items?page=3");
        assertThat(pagination.getPreviousPageLink()).isEqualTo("/items?page=1");
        assertThat(pagination.getNextPageLink()).isEqualTo("/items?page=3");
    }
}
