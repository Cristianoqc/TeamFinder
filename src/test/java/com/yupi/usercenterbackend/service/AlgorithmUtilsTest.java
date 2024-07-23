package com.yupi.usercenterbackend.service;

import com.yupi.usercenterbackend.utils.AlgorithmUtils;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

/**
 * 算法工具类测试
 */
class AlgorithmUtilsTest {
    /**
     * 编辑距离算法测试
     */
    @Test
    void test(){
        List<String> l1 = Arrays.asList("java", "大一", "男");
        List<String> l2 = Arrays.asList("java", "大二", "女");
        int n = AlgorithmUtils.minDistance(l1, l2);
        System.out.println(n);
    }
}
