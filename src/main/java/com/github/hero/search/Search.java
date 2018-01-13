package com.github.hero.search;

import java.io.IOException;
import java.util.concurrent.Callable;

/**
 * User: benjamin.wuhaixu
 * Date: 2018-01-13
 * Time: 00:56 am
 */
public interface Search extends Callable {

    /**
     * 搜索关键字得到相关结果个数
     * @return
     * @throws IOException
     */
    Long search() throws IOException;

    /**
     * 搜索第一页中keyword的出现个数
     * @param keyword
     * @return
     */
    Long searchFirstPageCount(String keyword) throws IOException;
}
