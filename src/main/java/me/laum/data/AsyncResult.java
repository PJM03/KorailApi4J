package me.laum.data;

import lombok.Getter;

@Getter
public class AsyncResult<T> {
    private volatile T data = null;

    public void setData(T data) {
        if (this.data != null) throw new IllegalStateException("data is already set.");
        this.data = data;
    }

    public T waitData() {
        while (data == null) Thread.onSpinWait();
        return data;
    }
}
