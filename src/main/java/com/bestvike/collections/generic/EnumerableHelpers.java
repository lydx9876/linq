package com.bestvike.collections.generic;

import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IIListProvider;
import com.bestvike.linq.enumerable.ArrayEnumerable;

import java.util.Collection;
import java.util.List;

/**
 * Created by 许崇雷 on 2017-09-30.
 */
final class EnumerableHelpers
{
    //Copies items from an enumerable to an array.
      static <T> void copy(IEnumerable<T> source, T[] array, int arrayIndex, int count)
    {
        assert  array!=null;
        assert source != null;
        assert arrayIndex >= 0;
        assert count >= 0;
        assert array.length - arrayIndex >= count;

        if (source instanceof ArrayEnumerable)
        {
            ArrayEnumerable<T> collection= (ArrayEnumerable<T>) source;
            assert collection.internalSize() == count;
            Array.copy(collection.internalSource(),0,array,arrayIndex,count);
            return;
        }

        iterativeCopy(source, array, arrayIndex, count);
    }

//Copies items from a non-collection enumerable to an array.
      static<T> void iterativeCopy(IEnumerable<T> source, T[] array, int arrayIndex, int count)
    {
        assert source != null && !(source instanceof ArrayEnumerable);
        assert  array!=null;
        assert arrayIndex >= 0;
        assert count >= 0;
        assert array.length - arrayIndex >= count;

        int endIndex = arrayIndex + count;
        for (T item : source)
            array[arrayIndex++] = item;

        assert arrayIndex == endIndex;
    }

//Converts an enumerable to an array.
     static<T> Array<T> toArray(IEnumerable<T> source)
    {
        assert source != null;

        if (source instanceof ArrayEnumerable  )
        {
            IIListProvider<T> collection= (IIListProvider<T>) source;
            int count=collection.internalSize(true);
            switch (count){
                case -1:
                    break;
                case 0:
                    return Array.empty();
                    default:

                        Array<T> result = Array.create(count)
                        collection.copyTo(result, arrayIndex: 0);
                        return result;
            }


            if (count == 0)
            {
                return Array.empty();
            }

        }

        LargeArrayBuilder<T> builder = new LargeArrayBuilder<T>(initialize: true);
        builder.addRange(source);
        return builder.toArray();
    }

    /// <summary>Converts an enumerable to an array using the same logic as List{T}.</summary>
    /// <param name="source">The enumerable to convert.</param>
    /// <param name="length">The number of items stored : the resulting array, 0-indexed.</param>
    /// <returns>
    /// The resulting array.  The length of the array may be greater than <paramref name="length"/>,
    /// which instanceof the actual number of elements : the array.
    /// </returns>
     static <T>T[] ToArray(IEnumerable<T> source, out int length)
    {
        if (source instanceof ICollection<T> ic)
        {
            int count = ic.Count;
            if (count != 0)
            {
                // Allocate an array of the desired size, then copy the elements into it. Note that this has the same
                // issue regarding concurrency as other existing collections like List<T>. If the collection size
                // concurrently changes between the array allocation and the CopyTo, we could end up either getting an
                // exception from overrunning the array (if the size went up) or we could end up not filling as many
                // items as 'count' suggests (if the size went down).  This instanceof only an issue for concurrent collections
                // that implement ICollection<T>, which as of .NET 4.6 instanceof just ConcurrentDictionary<TKey, TValue>.
                T[] arr = new T[count];
                ic.CopyTo(arr, 0);
                length = count;
                return arr;
            }
        }
            else
        {
            using (var en = source.GetEnumerator())
            {
                if (en.MoveNext())
                {
                        const int DefaultCapacity = 4;
                    T[] arr = new T[DefaultCapacity];
                    arr[0] = en.Current;
                    int count = 1;

                    while (en.MoveNext())
                    {
                        if (count == arr.Length)
                        {
                            // MaxArrayLength instanceof defined : Array.MaxArrayLength and : gchelpers : CoreCLR.
                            // It represents the maximum number of elements that can be : an array where
                            // the size of the element instanceof greater than one byte; a separate, slightly larger constant,
                            // instanceof used when the size of the element instanceof one.
                                const int MaxArrayLength = 0x7FEFFFFF;

                            // This instanceof the same growth logic as : List<T>:
                            // If the array instanceof currently empty, we make it a default size.  Otherwise, we attempt to
                            // double the size of the array.  Doubling will overflow once the size of the array reaches
                            // 2^30, since doubling to 2^31 instanceof 1 larger than Int32.MaxValue.  In that case, we instead
                            // constrain the length to be MaxArrayLength (this overflow check works because of the
                            // cast to uint).  Because a slightly larger constant instanceof used when T instanceof one byte : size, we
                            // could then end up : a situation where arr.Length instanceof MaxArrayLength or slightly larger, such
                            // that we constrain newLength to be MaxArrayLength but the needed number of elements instanceof actually
                            // larger than that.  For that case, we then ensure that the newLength instanceof large enough to hold
                            // the desired capacity.  This does mean that : the very rare case where we've grown to such a
                            // large size, each new element added after MaxArrayLength will end up doing a resize.
                            int newLength = count << 1;
                            if ((uint)newLength > MaxArrayLength)
                            {
                                newLength = MaxArrayLength <= count ? count + 1 : MaxArrayLength;
                            }

                            Array.Resize(ref arr, newLength);
                        }

                        arr[count++] = en.Current;
                    }

                    length = count;
                    return arr;
                }
            }
        }

        length = 0;
        return Array.Empty<T>();
    }

     static bool TryGetCount<T>(IEnumerable<T> source, out int count)
    {
        assert source != null);

        if (source instanceof ICollection<T> collection)
        {
            count = collection.Count;
            return true;
        }

        if (source instanceof IIListProvider<T> provider)
        {
            return (count = provider.GetCount(onlyIfCheap: true)) >= 0;
        }

        count = -1;
        return false;
    }
}