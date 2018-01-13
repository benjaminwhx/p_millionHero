package com.github.hero;

import com.github.hero.algorithm.Algorithm;
import com.github.hero.algorithm.BaseAlgorithm;
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

    private static final String DEFAULT_IMAGE = "D:\\test\\";
    private static long startTime;

    public static void main(String[] args) throws InterruptedException {
        while (true) {
            try {
                startTime = System.currentTimeMillis();
                // 1、查找图片
                File file = findImage();
                if (file == null) {
                    continue;
                }

                // 延迟500ms
                Thread.sleep(500);

                // 2、检测到存在调用OCR识别
                String result = getOCRResult(file);
                System.out.println("识别成功，花费时间：" + (System.currentTimeMillis() - startTime) + "，结果为：" + result);
                if (result.contains("error_code")) {
                    System.out.println("图片解析错误，请重新截图进行重试...");
                    continue;
                }

                // 3、对识别出来的结果获取问题和答案
                Information information = StringUtils.getInformationByOCR(result);;
                String question = information.getQuestion();
                String[] answers = information.getAnswers();
                if (StringUtils.isEmpty(question) || answers == null) {
//                    FileUtils.deleteQuietly(file);
                    System.out.println("问题识别失败，请重新截图进行重试...");
                    continue;
                }

                // 4、打印问题和答案
                System.out.println("开始识别问题和答案...");
                System.out.println("问题为:" + question);
                System.out.println("答案选项为:");
                for (int i = 0; i < answers.length; ++i) {
                    System.out.println(i + "、" + answers[i]);
                }

                Algorithm algorithm = new BaseAlgorithm();
                String optimizeResult = algorithm.calResult(question, answers);

                System.out.println("--------最终结果-------");
                System.out.println(optimizeResult);

                float excTime = (float) (System.currentTimeMillis() - startTime) / 1000;
                System.out.println("执行时间：" + excTime + "s");
//                FileUtils.deleteQuietly(file);
            } catch (Exception e) {
//                    FileUtils.deleteQuietly(file);
                continue;
            }
        }
    }

    /**
     * 找出默认目录下最新生成的图片（最新修改时间要 > 系统开始时间）
     * @return
     */
    private static File findImage() {
        File folder = new File(DEFAULT_IMAGE);
        String[] fileList = folder.list();
        for (String f :fileList) {
            if (f.endsWith("png") || f.endsWith("jpg") || f.endsWith("jpeg")) {
                File imageFile = new File(DEFAULT_IMAGE + f);
                if (imageFile.lastModified() < startTime) {
                    continue;
                }
                return imageFile;
            }
        }
        return null;
    }

    /**
     * 返回ocr检测的结果
     * @return
     */
    private static String getOCRResult(File imageFile) {
        OCR ocr = new BaiDuOCR();
        String result = ocr.getOCR(imageFile);
        return result;
    }
}
