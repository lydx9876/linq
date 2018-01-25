package com.bestvike.linq;

import com.bestvike.Tuple2;

/**
 * Created by 许崇雷 on 2017-09-30.
 */
public interface IPartition<TElement> extends IIListProvider<TElement> {
    IPartition<TElement> internalSkip(int count);

    IPartition<TElement> internalTake(int count);

    Tuple2<Boolean, TElement> internalTryGetElementAt(int index);

    Tuple2<Boolean, TElement> internalTryGetFirst();

    Tuple2<Boolean, TElement> internalTryGetLast();
}
