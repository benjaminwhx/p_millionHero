package com.github.hero.search;

import java.io.IOException;
import java.util.concurrent.Callable;

/**
 * User: benjamin.wuhaixu
 * Date: 2018-01-13
 * Time: 00:56 am
 */
public interface Search extends Callable {

    Long search() throws IOException;
}
