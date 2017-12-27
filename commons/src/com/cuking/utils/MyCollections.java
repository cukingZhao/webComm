package com.cuking.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: 集合操作类
 * @author: cuking
 * @date: 2017/11/27
 */
public class MyCollections {


    /**
     * ,分割的字符串转 List<Integer></>
     * @param s
     * @return
     */
    public static List<Integer> string2IntegerList(String s){
        String[] strings = s.split(",");
        List<Integer> res = new ArrayList<>(strings.length);
        for (String ss : strings){
            res.add(Integer.parseInt(ss));
        }
        return res;
    }
}
