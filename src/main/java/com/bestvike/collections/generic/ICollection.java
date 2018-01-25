package com.bestvike.collections.generic;

/**
 * Created by 许崇雷 on 2017-09-30.
 */
public interface ICollection<T> {
    int size();

    void copyTo(T[] array, int arrayIndex);

    void copyTo(Array<T> array, int arrayIndex);
}
