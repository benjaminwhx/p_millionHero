package com.github.hero.algorithm;

import java.util.concurrent.ExecutionException;

/**
 * User: 吴海旭
 * Date: 2018-01-13
 * Time: 下午3:51
 */
public interface Algorithm {

    /**
     * 计算最优结果
     * @param question 问题
     * @param answers 答案选项
     * @return
     */
    String calResult(String question, String[] answers) throws ExecutionException, InterruptedException;
}
