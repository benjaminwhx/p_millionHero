package com.github.hero.utils;

import com.github.hero.bean.Information;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * User: benjamin.wuhaixu
 * Date: 2018-01-13
 * Time: 00:58 am
 */
public class StringUtils {

    private static final List<String> BLACK_LIST = new ArrayList<>();

    static {
        BLACK_LIST.add("点击查看源网页");
    }

    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    public static Information getInformationByOCR(String ocrResult) {
        Information information = new Information();
        JSONObject jsonObject = JSONUtils.fromObject(ocrResult);
        JSONArray jsonArray = (JSONArray)jsonObject.get("words_result");
        int size = jsonArray.size();
        StringBuilder question = new StringBuilder();
        int endIndex = 0;
        for (int i = 0; i < size; ++i) {
            JSONObject json = JSONUtils.fromObject(jsonArray.getString(i));
            String data = json.getString("words");
            if (i == 0 && Character.isDigit(data.charAt(0))) {
                // 去除题数
                if (data.charAt(1) != '.') {
                    data = data.substring(1);
                } else {
                    data = data.substring(2);
                }
            }
            if (data.endsWith("?")) {
                question.append(data);
                endIndex = i;
                break;
            } else if (i == 1) {
                endIndex = 0;
                break;
            }
            question.append(data);
        }
        information.setQuestion(question.toString());

        List<String> answers = new ArrayList<>();
        for (int i = endIndex + 1; i < size; ++i) {
            JSONObject json = JSONUtils.fromObject(jsonArray.getString(i));
            String data = json.getString("words");
            if (!BLACK_LIST.contains(data)) {
                answers.add(data);
            }
        }
        information.setAnswers(answers.toArray(new String[0]));
        return information;
    }

}
