package com.github.hero.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        List<Integer> usedIndex = new ArrayList<>(floats.length);
        for (int i = 0; i < floats.length; i++) {
            for (int j = 0; j < floats.length; j++) {
                if (f[i] == floats[j] && !usedIndex.contains(j)) {
                    usedIndex.add(j);
                    rank[i] = j;
                    break;
                }
            }
        }
        return rank;
    }

    public static long getKeywordCount(String line, String keyword) {
        int i = line.length() - line.replace(keyword, "").length();
        return i / keyword.length();
    }

    public static void main(String[] args) {
        float[] f = new float[3];
        f[0] = 1.1f;
        f[1] = 3.1f;
        f[2] = 2.1f;
        int[] rank = rank(f);
        System.out.println(Arrays.toString(rank));
        String string = "javajava_eclipse_class_jajavavajavajdjdj";
        String str = "java";
        long keywordCount = getKeywordCount(string, str);
        System.out.println(keywordCount);
    }
}
