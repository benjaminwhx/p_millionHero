package com.github.hero.ocr;

import java.io.File;

/**
 * User: benjamin.wuhaixu
 * Date: 2018-01-13
 * Time: 00:56 am
 */
public interface OCR {

    /**
     *获取识别图片后的结果
     * @param file String
     * @return String
     */
    String getOCR(File file);
}
