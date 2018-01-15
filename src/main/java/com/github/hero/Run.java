package com.github.hero;

import com.github.hero.algorithm.Algorithm;
import com.github.hero.algorithm.BaseAlgorithm;
import com.github.hero.bean.Information;
import com.github.hero.ocr.BaiDuOCR;
import com.github.hero.ocr.OCR;
import com.github.hero.search.BaiDuSearch;
import com.github.hero.search.Search;
import com.github.hero.utils.HttpClientUtil;
import com.github.hero.utils.JSONUtils;
import com.github.hero.utils.StringUtils;
import com.github.hero.utils.Utils;
import net.sf.json.JSONObject;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.Scanner;
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
        Scanner scanner = new Scanner(System.in);
        System.out.println("请选择使用的方式：\n1、图片轮训百度方式\n2、搜狗API方式");
        String line = scanner.nextLine();
        int num = 0;
        while (true) {
            if ("1".equals(line)) {
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
                } catch (Exception e) {
                    continue;
                }
            } else if ("2".equals(line)) {
                // http://answer.sm.cn/answer/curr?format=json&activity=million&_t=1515985188454&activity=million
                // {"status":0,"data":{"title":"","time":1515985053,"status":1,"round":"","correct":"","options":[],"sid":"103"}}
                // {"status":0,"data":{"title":"回锅肉属于下列哪一种菜系？","time":1515985523,"status":1,"round":"1","correct":"1","options":[{"confidence":"159.910415649","score":"29.1001044476","title":"鲁菜"},{"confidence":"241.178756714","score":"43.8891174315","title":"川菜"},{"confidence":"148.42918396","score":"27.0107781209","title":"湘菜"}],"sid":"103"}}
                // {"status":0,"data":{"title":"在吴承恩所著《西游记》中，白骨精第三次变成了什么人来迷惑唐僧？","time":1515985862,"status":1,"round":"5","correct":"2","options":[{"confidence":"146.817581177","score":"23.7569253525","title":"女孩"},{"confidence":"235.344009399","score":"38.0816113344","title":"老妇"},{"confidence":"235.837493896","score":"38.1614633131","title":"老翁"}],"sid":"103"}}
				// {"status":0,"data":{"title":"1、2、3、4四个数排列组合成四位数，且数字不重复，有多少种组合方式？","time":1515986354,"status":1,"round":"10","correct":"1","options":[{"confidence":"228.068222046","score":"32.5051203678","title":"18"},{"confidence":"245.50138855","score":"34.9897592645","title":"24"},{"confidence":"228.068222046","score":"32.5051203678","title":"36"}],"sid":"103"}}
				Thread.sleep(10);
                String result = HttpClientUtil.get("http://answer.sm.cn/answer/curr?format=json&activity=million&_t=" + System.currentTimeMillis() + "&activity=million");
                JSONObject jsonObject = JSONUtils.fromObject(result);
                if (jsonObject.has("data")) {
                    String data = jsonObject.getString("data");
                    JSONObject dataJson = JSONUtils.fromObject(data);
                    String round = dataJson.getString("round");
                    if ((StringUtils.isNotEmpty(round) && Integer.parseInt(round) > num)) {
                        String title = dataJson.getString("title");
                        String correct = dataJson.getString("correct");
                        if (StringUtils.isNotEmpty(title) && StringUtils.isNotEmpty(correct)) {
                            System.out.println(round + "、" + title);
                            System.out.println("答案：选" + String.valueOf(Integer.parseInt(correct) + 1));
                            num = Integer.parseInt(round);
                        }
                    }
                }
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
