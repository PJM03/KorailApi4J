package me.laum.data;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PageResult<T> {
    private final int page;
    private final int totalCount;

    private final T data;

    public boolean hasNext() {
        return page < totalCount / 10;
    }
}
