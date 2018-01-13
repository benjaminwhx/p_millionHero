package com.github.hero.algorithm;

import com.github.hero.search.BaiDuSearch;
import com.github.hero.utils.Utils;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * User: 吴海旭
 * Date: 2018-01-13
 * Time: 下午3:52
 */
public class BaseAlgorithm implements Algorithm {

    private ExecutorService threadPool = Executors.newFixedThreadPool(20, new ThreadFactory() {
        private AtomicInteger num = new AtomicInteger(0);
        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "baseAlgorithm_" + num.incrementAndGet());
        }
    });

    @Override
    public String calResult(String question, String[] answers) throws ExecutionException, InterruptedException {
        int numOfAnswer = answers.length;

        /**
         * 结果存放
         */
        long countQuestion;
        long[] countQA = new long[numOfAnswer];
        long[] countAnswer = new long[numOfAnswer];

        Future<Long> questionFuture = threadPool.submit(new BaiDuSearch(question));
        Future<Long>[] qaFuture = new Future[numOfAnswer];
        Future<Long>[] aFuture = new Future[numOfAnswer];
        for (int i = 0; i < numOfAnswer; ++i) {
            String a = answers[i];
            String qa = question + " " + a;
            qaFuture[i] = threadPool.submit(new BaiDuSearch(qa));
            aFuture[i] = threadPool.submit(new BaiDuSearch(a));
        }

        countQuestion = questionFuture.get();
        for (int i = 0; i < numOfAnswer; ++i) {
            countQA[i] = qaFuture[i].get();
            countAnswer[i] = aFuture[i].get();
        }

        int maxIndex = 0;

        /**
         * 相关性
         */
        float[] ans = new float[numOfAnswer];
        for (int i = 0; i < numOfAnswer; i++) {
            ans[i] = (float) countQA[i] / (float) (countQuestion * countAnswer[i]);
            maxIndex = (ans[i] > ans[maxIndex]) ? i : maxIndex;
        }
//        //根据pmi值进行打印搜索结果
        int[] rank = Utils.rank(ans);
        for (int i : rank) {
            System.out.print(answers[i]);
            System.out.print(" countQA:" + countQA[i]);
            System.out.print(" countQ:" + countQuestion);
            System.out.print(" countAnswer:" + countAnswer[i]);
            System.out.println(" ans:" + ans[i]);
        }
        return answers[maxIndex];
    }
}
