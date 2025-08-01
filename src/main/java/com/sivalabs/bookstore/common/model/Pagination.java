package com.sivalabs.bookstore.common.model;

import java.util.HashMap;
import java.util.Map;

public class Pagination<T> {
    private final PagedResult<T> pagedResult;
    private final String paginationUrlPrefix;
    private final Map<String, Object> queryParams;

    public Pagination(PagedResult<T> pagedResult, String paginationUrlPrefix) {
        this(pagedResult, paginationUrlPrefix, Map.of());
    }

    public Pagination(PagedResult<T> pagedResult, String paginationUrlPrefix, Map<String, Object> queryParams) {
        this.pagedResult = pagedResult;
        this.paginationUrlPrefix = paginationUrlPrefix;
        this.queryParams = queryParams;
    }

    public boolean hasData() {
        return !pagedResult.data().isEmpty();
    }

    public int currentPageNo() {
        return pagedResult.currentPageNo();
    }

    public int totalPages() {
        return pagedResult.totalPages();
    }

    public long totalElements() {
        return pagedResult.totalElements();
    }

    public boolean isFirstPage() {
        return hasData() && pagedResult.currentPageNo() == 1;
    }

    public boolean isLastPage() {
        return hasData() && pagedResult.currentPageNo() == pagedResult.totalPages();
    }

    public boolean hasNextPage() {
        return hasData() && pagedResult.hasNextPage();
    }

    public boolean hasPreviousPage() {
        return hasData() && pagedResult.hasPreviousPage();
    }

    public String getFirstPageLink() {
        if (!hasData() || isFirstPage()) {
            return "#";
        }
        Map<String, Object> queryParamsMap = new HashMap<>(Map.copyOf(this.queryParams));
        queryParamsMap.put("page", 1);
        return paginationUrlPrefix + "?" + getQueryString(queryParamsMap);
    }

    public String getLastPageLink() {
        if (!hasData() || isLastPage()) {
            return "#";
        }
        Map<String, Object> queryParamsMap = new HashMap<>(Map.copyOf(this.queryParams));
        queryParamsMap.put("page", pagedResult.totalPages());
        return paginationUrlPrefix + "?" + getQueryString(queryParamsMap);
    }

    public String getPreviousPageLink() {
        if (!hasData() || !hasPreviousPage()) {
            return "#";
        }
        Map<String, Object> queryParamsMap = new HashMap<>(Map.copyOf(this.queryParams));
        queryParamsMap.put("page", pagedResult.currentPageNo() - 1);
        return paginationUrlPrefix + "?" + getQueryString(queryParamsMap);
    }

    public String getNextPageLink() {
        if (!hasData() || !hasNextPage()) {
            return "#";
        }
        Map<String, Object> queryParamsMap = new HashMap<>(Map.copyOf(this.queryParams));
        queryParamsMap.put("page", pagedResult.currentPageNo() + 1);
        return paginationUrlPrefix + "?" + getQueryString(queryParamsMap);
    }

    private String getQueryString(Map<String, Object> queryParams) {
        if (queryParams.isEmpty()) {
            return "";
        }
        return queryParams.entrySet().stream()
                .map(e -> e.getKey() + "=" + e.getValue())
                .reduce((s1, s2) -> s1 + "&" + s2)
                .orElse("");
    }
}
