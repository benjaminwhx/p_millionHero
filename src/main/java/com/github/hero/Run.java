package com.github.hero;

import com.github.hero.bean.Information;
import com.github.hero.ocr.BaiDuOCR;
import com.github.hero.ocr.OCR;
import com.github.hero.search.BaiDuSearch;
import com.github.hero.search.Search;
import com.github.hero.utils.StringUtils;
import com.github.hero.utils.Utils;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.concurrent.*;

/**
 * User: benjamin.wuhaixu
 * Date: 2018-01-13
 * Time: 01:49 am
 */
public class Run {

    private static final String DEFAULT_IMAGE = "/Users/Benjamin/Desktop/Mac.png";

    public static void main(String[] args) {
        while (true) {
            long startTime = System.currentTimeMillis();
            File file = new File(DEFAULT_IMAGE);
            // 1、检测桌面是否有图片
            if (!file.exists()) {
                continue;
            }

            // 2、检测到存在调用OCR识别
            OCR ocr = new BaiDuOCR();
            String result = ocr.getOCR(file);
            System.out.println("识别成功，花费时间：" + (System.currentTimeMillis() - startTime));

            // 3、对识别出来的结果获取问题和答案
            Information information;
            String question;
            String[] answers;
            try {
                information = StringUtils.getInformationByOCR(result);
                question = information.getQuestion();
                answers = information.getAnswers();
                if (StringUtils.isEmpty(question) || answers == null) {
                    FileUtils.deleteQuietly(file);
                    System.out.println("问题识别失败，请重新截图进行重试...");
                    continue;
                }
            } catch (Exception e) {
                e.printStackTrace();
                FileUtils.deleteQuietly(file);
                continue;
            }

            System.out.println("开始识别问题和答案...");
            System.out.println("问题为:" + question);
            System.out.println("答案选项为:");
            for (int i = 0; i < answers.length; ++i) {
                System.out.println(i + "、" + answers[i]);
            }

            long countQuestion = 1;
            int numOfAnswer = answers.length;
            long[] countQA = new long[numOfAnswer];
            long[] countAnswer = new long[numOfAnswer];

            int maxIndex = 0;

            Search[] searchQA = new Search[numOfAnswer];
            Search[] searchAnswers = new Search[numOfAnswer];
            FutureTask[] futureQA = new FutureTask[numOfAnswer];
            FutureTask[] futureAnswers = new FutureTask[numOfAnswer];
            FutureTask<Long> futureQuestion = new FutureTask<Long>(new BaiDuSearch(question));
            new Thread(futureQuestion).start();

            for (int i = 0; i < numOfAnswer; i++) {
                searchQA[i] = new BaiDuSearch((question + " " + answers[i]));
                searchAnswers[i] = new BaiDuSearch(answers[i]);

                futureQA[i] = new FutureTask<Long>(searchQA[i]);
                futureAnswers[i] = new FutureTask<Long>(searchAnswers[i]);
                new Thread(futureQA[i]).start();
                new Thread(futureAnswers[i]).start();
            }
            try {
                // 阻塞等待问题的count
                countQuestion = futureQuestion.get();
                for (int i = 0; i < numOfAnswer; i++) {
                    countQA[i] = (Long) futureQA[i].get();
                    countAnswer[i] = (Long) futureAnswers[i].get();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            float[] ans = new float[numOfAnswer];
            for (int i = 0; i < numOfAnswer; i++) {
                ans[i] = (float) countQA[i] / (float) (countQuestion * countAnswer[i]);
                maxIndex = (ans[i] > ans[maxIndex]) ? i : maxIndex;
            }
            //根据pmi值进行打印搜索结果
            int[] rank = Utils.rank(ans);
            for (int i : rank) {
                System.out.print(answers[i]);
                System.out.print(" countQA:" + countQA[i]);
                System.out.print(" countQ:" + countQuestion);
                System.out.print(" countAnswer:" + countAnswer[i]);
                System.out.println(" ans:" + ans[i]);
            }

            System.out.println("--------最终结果-------");
            System.out.println(answers[maxIndex]);
            float excTime = (float) (System.currentTimeMillis() - startTime) / 1000;

            System.out.println("执行时间：" + excTime + "s");
            FileUtils.deleteQuietly(file);
        }
    }
}
