package com.haihua.hhplms.common.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListUtils {

    /**
     * 获取first集合和second集合的交集
     */
    public static <T> List<T> getIntersection(List<T> first, List<T> second) {
        List<T> copyOfFirst = new ArrayList<>(first);
        copyOfFirst.retainAll(second);
        return copyOfFirst;
    }

    /**
     * 获取first集合中存在但在second集合中不存在的元素集合
     */
    public static <T> List<T> getDifference(List<T> first, List<T> second) {
        List<T> copyOfFirst = new ArrayList<>(first);
        copyOfFirst.removeAll(second);
        return copyOfFirst;
    }

    public static void main(String [] args) {
        List<Integer> first = Arrays.asList(new Integer [] {1, 2});
        List<Integer> second = Arrays.asList(new Integer [] {3, 4});

        System.out.println(first);
        System.out.println(second);
        System.out.println(getIntersection(first, second));
        System.out.println(getDifference(first, getIntersection(first, second)));
        System.out.println(getDifference(second, getIntersection(first, second)));
    }
}
