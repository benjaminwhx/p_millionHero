package com.github.hero.search;

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
    public Object call() throws Exception {
        return search();
    }

    public static void main(String[] args) throws IOException {
        Search search = new BaiDuSearch("泰国");
        Search search1 = new BaiDuSearch("泰国");
        Search search2 = new BaiDuSearch("泰国");
        System.out.println(search.search());
        System.out.println(search1.search());
        System.out.println(search2.search());
    }
}
