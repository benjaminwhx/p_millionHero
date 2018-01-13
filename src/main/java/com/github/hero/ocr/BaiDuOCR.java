package com.github.hero.ocr;

import com.baidu.aip.ocr.AipOcr;
import com.github.hero.bean.Information;
import com.github.hero.utils.StringUtils;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;

/**
 * User: benjamin.wuhaixu
 * Date: 2018-01-13
 * Time: 00:56 am
 */
public class BaiDuOCR implements OCR {

    //设置APPID/AK/SK
    private static final String APP_ID = "10676706";
    private static final String API_KEY = "LUCojrtX9sGTh4pHn1nIa9zT";
    private static final String SECRET_KEY = "39IcxEGfLABxNG68jQ0AKamPM2t0rhHW";
    private static final AipOcr CLIENT = new AipOcr(APP_ID, API_KEY, SECRET_KEY);

    public BaiDuOCR(){
        // 可选：设置网络连接参数
        CLIENT.setConnectionTimeoutInMillis(2000);
        CLIENT.setSocketTimeoutInMillis(60000);
    }

    @Override
    public String getOCR(File file) {
        String path = file.getAbsolutePath();
        // 调用接口
        JSONObject res = CLIENT.basicGeneral(path, new HashMap<String, String>());
        return res.toString(2);
    }

    public static void main(String[] args) {
        OCR ocr = new BaiDuOCR();
        String path = "/Users/Benjamin/Desktop/a.png";
        String result = ocr.getOCR(new File(path));
        System.out.println(result);

        Information information = StringUtils.getInformationByOCR(result);
        System.out.println(information);
    }
}
