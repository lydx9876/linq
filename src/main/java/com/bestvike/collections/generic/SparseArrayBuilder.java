package com.bestvike.collections.generic;

import com.bestvike.linq.IEnumerable;

/**
 * Created by 许崇雷 on 2017-09-30.
 */
final class   Marker {//struct
        private final int count;
        private final int index;

        public Marker(int count, int index) {
                assert count >= 0;
                assert index >= 0;

               this. count = count;
                this.  index = index;
        }


        public int count() {
                return this.count;
        }


        public int index() {
                return this.index;
        }

        @Override
        public String toString() {
                return String.format("index: %s, count: %s", this.index, this.count);
        }

}


   final   class SparseArrayBuilder<T>//struct
    {
private LargeArrayBuilder<T> _builder= new LargeArrayBuilder<T>( );
private ArrayBuilder<Marker> _markers =new ArrayBuilder<>();
private int _reservedCount;



public int count() {
   return  Math.addExact(_builder.count(),_reservedCount) ;}

/// <summary>
/// The list of reserved regions in this builder.
/// </summary>
public ArrayBuilder<Marker> markers (){
        return  _markers;
}

/// <summary>
/// Adds an item to this builder.
/// </summary>
/// <param name="item">The item to add.</param>
public void add(T item) {
        _builder.add(item);
}


public void addRange(IEnumerable<T> items) { _builder.addRange(items);};


public void CopyTo(T[] array, int arrayIndex, int count)
        {
        assert array != null;
        assert arrayIndex >= 0;
        assert count >= 0 && count <= this.count() ;
        assert array.length - arrayIndex >= count;

        int copied = 0;
                CopyPosition position = CopyPosition.start();

        for (int i = 0; i < _markers.count(); i++)
        {
        Marker marker =_markers.get(i);

        // During this iteration, copy until we satisfy `count` or reach the marker.
        int toCopy = Math.min(marker.index() - copied, count);

        if (toCopy > 0)
        {
        position = _builder.copyTo(position, array, arrayIndex, toCopy);

        arrayIndex += toCopy;
        copied += toCopy;
        count -= toCopy;
        }

        if (count == 0)
        {
        return;
        }

        // We hit our marker. Advance until we satisfy `count` or fulfill `marker.Count`.
        int reservedCount = Math.min(marker.count(), count);

        arrayIndex += reservedCount;
        copied += reservedCount;
        count -= reservedCount;
        }

        if (count > 0)
        {
        // Finish copying after the final marker.
        _builder.copyTo(position, array, arrayIndex, count);
        }
     }

/// <summary>
/// Reserves a region starting from the current index.
/// </summary>
/// <param name="count">The number of items to reserve.</param>
/// <remarks>
/// This method will not make optimizations if <paramref name="count"/>
/// is zero; the caller is responsible for doing so. The reason for this
/// is that the number of markers needs to match up exactly with the number
/// of times <see cref="Reserve"/> was called.
/// </remarks>
public void Reserve(int count)
        {
        assert count >= 0;

        _markers.add(new Marker(  count,   count()));
        _reservedCount=Math.addExact(_reservedCount, count);
        }

/// <summary>
/// Reserves a region if the items' count can be predetermined; otherwise, adds the items to this builder.
/// </summary>
/// <param name="items">The items to reserve or add.</param>
/// <returns><c>true</c> if the items were reserved; otherwise, <c>false</c>.</returns>
/// <remarks>
/// If the items' count is predetermined to be 0, no reservation is made and the return value is <c>false</c>.
/// The effect is the same as if the items were added, since adding an empty collection does nothing.
/// </remarks>
public boolean ReserveOrAdd(IEnumerable<T> items)
        {
        int itemCount;
        if (EnumerableHelpers.TryGetCount(items, out itemCount))
        {
        if (itemCount > 0)
        {
        Reserve(itemCount);
        return true;
        }
        }
        else
        {
        AddRange(items);
        }
        return false;
        }

/// <summary>
/// Creates an array from the contents of this builder.
/// </summary>
/// <remarks>
/// Regions created with <see cref="Reserve"/> will be default-initialized.
/// </remarks>
public T[] ToArray()
        {
        // If no regions were reserved, there are no 'gaps' we need to add to the array.
        // In that case, we can just call ToArray on the underlying builder.
        if (_markers.Count == 0)
        {
        assert _reservedCount == 0);
        return _builder.ToArray();
        }

        var array = new T[Count];
        CopyTo(array, 0, array.Length);
        return array;
        }
        }