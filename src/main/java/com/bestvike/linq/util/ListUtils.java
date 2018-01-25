package com.bestvike.linq.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 许崇雷 on 2017/7/19.
 */
public final class ListUtils {
    private ListUtils() {
    }

    public static <T> List<T> empty() {
        return new ArrayList<>(0);
    }
}
