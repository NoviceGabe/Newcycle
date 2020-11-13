package com.example.newcycle.Interface;

import java.util.List;

public interface OnReadClient<T> {
    void onSuccess(List<T> results);
    void onError(String message);
}
