package com.github.hero.search;

import com.github.hero.utils.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * User: benjamin.wuhaixu
 * Date: 2018-01-13
 * Time: 01:48 am
 */
public class BaiDuSearch implements Search {
    private String question;
    private String path;

    public BaiDuSearch(String question) {
        this.question = question;
        try {
            this.path = "http://www.baidu.com/s?tn=ichuner&lm=-1&word=" +
                    URLEncoder.encode(question, "UTF-8") + "&rn=1";
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public Long search() throws IOException {
        boolean findIt = false;
        String line = null;
        while (!findIt) {
            URL url = new URL(path);
            BufferedReader breaded = new BufferedReader(new InputStreamReader(url.openStream(),"UTF-8"));
            while ((line = breaded.readLine()) != null) {
                if (line.contains("百度为您找到相关结果约")) {
                    findIt = true;
                    int start = line.indexOf("百度为您找到相关结果约") + 11;

                    line = line.substring(start);
                    int end = line.indexOf("个");
                    line = line.substring(0, end);
                    break;
                }

            }
        }
        line = line.replace(",", "");
        return Long.valueOf(line);
    }

    @Override
    public Long searchFirstPageCount(String keyword) throws IOException {
        int page = 1;
        int pageSize = 100;
        URL url = new URL("http://www.baidu.com/s?tn=ichuner&lm=-1&word=" + question + "&rn=100");
        BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream(),"UTF-8"));
        long keywordCount = 0;
        String line;
        while ((line = br.readLine()) != null) {
            System.out.println(line);
            keywordCount += Utils.getKeywordCount(line, keyword);
        }
        return keywordCount;
    }

    @Override
    public Object call() throws Exception {
        return search();
    }

    public static void main(String[] args) throws IOException {
        Search search = new BaiDuSearch("北京处于下列哪一大陆板块?");
        Long a = search.searchFirstPageCount("亚欧板块");
        Long b = search.searchFirstPageCount("南极洲板块");
        Long c = search.searchFirstPageCount("非洲板块");
        System.out.println(a);
        System.out.println(b);
        System.out.println(c);
    }
}
