package com.github.hero.utils;

import java.util.Arrays;

/**
 * User: benjamin.wuhaixu
 * Date: 2018-01-13
 * Time: 02:04 am
 */
public class Utils {

    /**
     * 对rank值进行排序
     *
     * @param floats pmi值
     * @return 返回排序的rank
     */
    public static int[] rank(float[] floats) {
        int[] rank = new int[floats.length];
        float[] f = Arrays.copyOf(floats, floats.length);
        Arrays.sort(f);
        for (int i = 0; i < floats.length; i++) {
            for (int j = 0; j < floats.length; j++) {
                if (f[i] == floats[j]) {
                    rank[i] = j;
                }
            }
        }
        return rank;
    }
}
