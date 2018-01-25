package com.bestvike.linq;

import com.bestvike.collections.generic.Array;

import java.util.List;

/**
 * Created by 许崇雷 on 2017-09-30.
 */
public interface IIListProvider<TElement> extends IEnumerable<TElement> {
    int internalSize(boolean onlyIfCheap);

    boolean internalContains(TElement value);

    TElement[] internalToArray(Class<TElement> clazz);

    Array<TElement> internalToArray();

    List<TElement> internalToList();
}
