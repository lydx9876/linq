package com.bestvike.linq.impl;

import com.bestvike.collections.generic.Array;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IIListProvider;
import com.bestvike.linq.util.ArrayUtils;

/**
 * Created by 许崇雷 on 2017/7/17.
 */
public final class Buffer<TElement> {
    private final Array<TElement> items;
    private final int count;

    public Buffer(IEnumerable<TElement> source) {
        Array<TElement> items = null;
        int count = 0;
        if (source instanceof IIListProvider) {
            IIListProvider<TElement> collection = (IIListProvider<TElement>) source;
            count = collection.internalSize();
            if (count > 0)
                items = collection.internalToArray();
        } else {
            for (TElement item : source) {
                if (items == null)
                    items = Array.create(4);
                else if (items.length() == count)
                    items.resize(Math.multiplyExact(count, 2));
                items.set(count, item);
                count++;
            }
        }
        this.items = items;
        this.count = count;
    }

    public int count() {
        return this.count;
    }

    public Array<TElement> items() {
        return this.items;
    }

    public TElement[] toArray(Class<TElement> clazz) {
        TElement[] array = ArrayUtils.newInstance(clazz, this.count);
        if (this.count > 0)
            Array.copy(this.items, 0, array, 0, this.count);
        return array;
    }
}
