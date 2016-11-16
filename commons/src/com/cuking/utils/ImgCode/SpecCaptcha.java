package com.cuking.utils.ImgCode;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

/**
 * <p>png格式验证码</p>
 *
 * @author: wuhongjun
 * @version:1.0
 */
public class SpecCaptcha extends Captcha {
    public SpecCaptcha() {
    }

    public SpecCaptcha(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public SpecCaptcha(int width, int height, int len) {
        this(width, height);
        this.len = len;
    }

    public SpecCaptcha(int width, int height, int len, Font font) {
        this(width, height, len);
        this.font = font;
    }

    /**
     * 生成验证码
     *
     * @throws IOException IO异常
     */
    @Override
    public void out(OutputStream out) {
        graphicsImage(rondomStrings(), out);
    }

    /**
     * 画随机码图
     *
     * @param strs 文本
     * @param out  输出流
     */
    private boolean graphicsImage(String[] strs, OutputStream out) {
        boolean ok = false;
        try {
            BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = (Graphics2D) bi.getGraphics();
            AlphaComposite ac3;
            Color color;
            int len = strs.length;
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, width, height);
            // 随机画干扰的蛋蛋
            for (int i = 0; i < 15; i++) {
                color = color(150, 250);
                g.setColor(color);
                g.drawOval(num(width), num(height), 1 + num(5), 1 + num(5));// 画蛋蛋，有蛋的生活才精彩
            }

            //绘制干扰线
            Random random = new Random();
            g.setColor(baseColor);// 设置线条的颜色
            for (int i = 0; i < lineNum; i++) {
                int x = random.nextInt(width - 1);
                int y = random.nextInt(height - 1);
                int xl = random.nextInt(6) + 1;
                int yl = random.nextInt(12) + 1;
                g.drawLine(x, y, x + xl + 40, y + yl + 20);
            }


            // 添加噪点
            float yawpRate = 0.02f;// 噪声率
            int area = (int) (yawpRate * width * height);
            for (int i = 0; i < area; i++) {
                int x = random.nextInt(width);
                int y = random.nextInt(height);
                int rgb = getRandomIntColor();
                bi.setRGB(x, y, rgb);
            }


            g.setFont(font);
            int h = height - ((height - font.getSize()) >> 1),
                    w = width / len,
                    size = w - font.getSize() + 1;
            /* 画字符串 */
            for (int i = 0; i < len; i++) {
                ac3 = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f);// 指定透明度
                g.setComposite(ac3);
                color = new Color(20 + num(110), 20 + num(110), 20 + num(110));// 对每个字符都用随机颜色
                g.setColor(color);
                g.drawString(strs[i] + "", (width - (len - i) * w) + size, h - 4);

            }

            shear(g, width, height, Color.WHITE);// 使图片扭曲


            ImageIO.write(bi, "png", out);
            out.flush();
            ok = true;
        } catch (IOException e) {
            ok = false;
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return ok;
    }
}