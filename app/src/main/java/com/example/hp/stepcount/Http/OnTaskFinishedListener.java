package com.example.hp.stepcount.Http;

/**
 * Created by bin on 10/02/2017.
 */
public interface OnTaskFinishedListener<T> {

    public void onTaskFinished(int status, T obj, Task task);
}
