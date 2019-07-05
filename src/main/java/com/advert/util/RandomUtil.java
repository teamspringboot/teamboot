package com.advert.util;

import org.apache.commons.lang.RandomStringUtils;

/**
 * 随机数工具类
 *
 * @author 
 * @since 1.0.0
 */
public final class RandomUtil {

    private RandomUtil() {
    }

    /**
     * 生成随机数
     */
    public static String getRandom(int count) {
        return RandomStringUtils.randomNumeric(count);
    }
}
