package com.bestvike.linq.partition;

import com.bestvike.Tuple;
import com.bestvike.Tuple2;
import com.bestvike.collections.generic.Array;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.IPartition;
import com.bestvike.linq.exception.Errors;
import com.bestvike.linq.util.ArrayUtils;
import com.bestvike.linq.util.ListUtils;

import java.util.List;

/**
 * Created by 许崇雷 on 2017-09-30.
 */
final class EmptyPartition<TElement> implements IPartition<TElement>, IEnumerator<TElement> {
    public static <TElement> IPartition<TElement> Instance() {
        return new EmptyPartition<>();
    }

    //构造函数
    private EmptyPartition() {
    }

    //region IEnumerable
    @Override
    public IEnumerator<TElement> enumerator() {
        return this;
    }
    //endregion

    //region IEnumerator
    @Override
    public boolean moveNext() {
        return false;
    }

    @Override
    public TElement current() {
        return null;
    }

    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public TElement next() {
        throw Errors.noSuchElement();
    }

    @Override
    public void reset() {
        throw Errors.notSupported();
    }

    @Override
    public void close() {
    }
    //endregion

    //region IIListProvider
    @Override
    public int internalSize(boolean onlyIfCheap) {
        return 0;
    }

    @Override
    public boolean internalContains(TElement value) {
        return false;
    }

    @Override
    public TElement[] internalToArray(Class<TElement> clazz) {
        return ArrayUtils.empty(clazz);
    }

    @Override
    public Array<TElement> internalToArray() {
        return Array.empty();
    }

    @Override
    public List<TElement> internalToList() {
        return ListUtils.empty();
    }
    //endregion

    //region IPartition
    @Override
    public IPartition<TElement> internalSkip(int count) {
        return this;
    }

    @Override
    public IPartition<TElement> internalTake(int count) {
        return this;
    }

    @Override
    public Tuple2<Boolean, TElement> internalTryGetElementAt(int index) {
        return Tuple.create(false, null);
    }

    @Override
    public Tuple2<Boolean, TElement> internalTryGetFirst() {
        return Tuple.create(false, null);
    }

    @Override
    public Tuple2<Boolean, TElement> internalTryGetLast() {
        return Tuple.create(false, null);
    }
    //endregion
}
