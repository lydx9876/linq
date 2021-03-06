package com.bestvike.linq.enumerator;

import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.exception.Errors;

/**
 * Created by 许崇雷 on 2017/7/17.
 */
public final class EmptyEnumerator<TSource> implements IEnumerator<TSource> {
    private EmptyEnumerator() {
    }

    public static <TSource> IEnumerator<TSource> Instance() {
        return new EmptyEnumerator<>();
    }

    @Override
    public boolean moveNext() {
        return false;
    }

    @Override
    public TSource current() {
        return null;
    }

    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public TSource next() {
        throw Errors.noSuchElement();
    }

    @Override
    public void reset() {
        throw Errors.notSupported();
    }

    @Override
    public void close() {
    }
}
