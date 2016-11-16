package com.cuking.utils.ImgCode;

import java.io.UnsupportedEncodingException;
import java.util.Random;

/**
 * <p>随机工具类</p>
 *
 * @author: wuhongjun
 * @version:1.0
 */
public class Randoms {
    private static final Random RANDOM = new Random();
    //定义验证码字符.去除了O和I等容易混淆的字母
    public static final String ALPHA[] = {"A", "B", "C", "D", "E", "F", "G", "H", "G", "K", "M", "N", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"
            , "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "m", "n", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "2", "3", "4", "5", "6", "7", "8", "9"};
//    public static final String ALPHA2[] = {"\u5b8b\u4f53", "\u65b0\u5b8b\u4f53",
//            "\u9ed1\u4f53", "\u6977\u4f53", "\u96b6\u4e66"};

    /**
     * 产生两个数之间的随机数
     *
     * @param min 小数
     * @param max 比min大的数
     * @return int 随机数字
     */
    public static int num(int min, int max) {
        return min + RANDOM.nextInt(max - min);
    }

    /**
     * 产生0--num的随机数,不包括num
     *
     * @param num 数字
     * @return int 随机数字
     */
    public static int num(int num) {
        return RANDOM.nextInt(num);
    }

    public static String alpha() {
        return getRandomChar();
    }


    public static String getRandomChar() {
        return ALPHA[num(ALPHA.length-1)];
    }

    /**
     * 生成随机汉字 原理是从汉字区位码找到汉字 在汉字区位码中分高位与底位， 其中有简体又有繁体，位数越前生成的汉字繁体的机率越大。
     * 在本例中高位从171取，底位从161取， 去掉大部分的繁体和生僻字，但仍然会有！！
     *
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String getRandomChinese() throws UnsupportedEncodingException {
        String str = null;
        // 定义高低位
        int hightPos, lowPos;
        Random random = new Random();
        // 获取高位值
        hightPos = (176 + Math.abs(random.nextInt(39)));
        // 获取低位值
        lowPos = (161 + Math.abs(random.nextInt(93)));
        byte[] b = new byte[2];
        b[0] = (new Integer(hightPos).byteValue());
        b[1] = (new Integer(lowPos).byteValue());
        // 转成中文
        str = new String(b, "GBK");
        return str;
    }
}