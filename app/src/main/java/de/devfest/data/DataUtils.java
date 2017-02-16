package de.devfest.data;

import java.util.List;

public final class DataUtils {

    private DataUtils() {
    }

    public static boolean equals(Object obj1, Object obj2) {
        return obj1 == null
                && obj2 == null
                || !(obj1 == null
                || obj2 == null)
                && obj1.equals(obj2);
    }

    public static boolean equals(List<?> list1, List<?> list2) {
        return list1 == null && list2 == null
                || !(list1 == null
                || list2 == null)
                && list1.size() == list2.size()
                && list1.containsAll(list2);
    }
}
