package com.cuking.utils.ImgCode;

import java.awt.*;
import java.io.OutputStream;
import java.util.Random;

/**
 * <p>
 * 验证码抽象类
 * </p>
 * 
 * @author: wuhongjun
 * @version:1.0
 */
public abstract class Captcha extends Randoms {
	Color baseColor = new Color(80, 140, 255);
	protected  int fontSize = 20;
	protected Font font = new Font("Verdana", Font.ITALIC | Font.BOLD, fontSize); // 字体
	protected int len = 4; // 验证码默认字符长度
	protected int width = 150; // 验证码显示跨度
	protected int height = 40; // 验证码显示高度
	protected int lineNum = 5; // 干扰线的数量
	protected String chars = ""; // 随机字符串


	private static Random random = new Random();

	/**
	 * 生成随机字符数组
	 * 
	 * @return 字符数组
	 */
	protected String[] rondomStrings() {
		String[] cs = new String[len];
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < len; i++) {
			cs[i] = alpha();
			sb.append(cs[i]);
		}
		chars = sb.toString();
		return cs;
	}

	public Font getFont() {
		return font;
	}

	public void setFont(Font font) {
		this.font = font;
	}

	public int getLen() {
		return len;
	}

	public void setLen(int len) {
		this.len = len;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	/**
	 * 给定范围获得随机颜色
	 * 
	 * @return Color 随机颜色
	 */
	protected Color color(int fc, int bc) {
		if (fc > 255)
			fc = 255;
		if (bc > 255)
			bc = 255;
		int r = fc + num(bc - fc);
		int g = fc + num(bc - fc);
		int b = fc + num(bc - fc);
		return new Color(r, g, b);
	}

	/**
	 * 验证码输出,抽象方法，由子类实现
	 * 
	 * @param os
	 *            输出流
	 */
	public abstract void out(OutputStream os);

	/**
	 * 获取随机字符串
	 * 
	 * @return string
	 */
	public String text() {
		return chars;
	}



	public  int getRandomIntColor() {
		int[] rgb = getRandomRgb();
		int color = 0;
		for (int c : rgb) {
			color = color << 8;
			color = color | c;
		}
		return color;
	}

	public  int[] getRandomRgb() {
		int[] rgb = new int[3];
		for (int i = 0; i < 3; i++) {
			rgb[i] = random.nextInt(255);
		}
		return rgb;
	}

	public  void shear(Graphics g, int w1, int h1, Color color) {
		shearX(g, w1, h1, color);
		shearY(g, w1, h1, color);
	}

	private static void shearX(Graphics g, int w1, int h1, Color color) {

		int period = random.nextInt(2);

		boolean borderGap = true;
		int frames = 1;
		int phase = random.nextInt(2);

		for (int i = 0; i < h1; i++) {
			double d = (double) (period >> 1)
					* Math.sin((double) i / (double) period
					+ (6.2831853071795862D * (double) phase)
					/ (double) frames);
			g.copyArea(0, i, w1, 1, (int) d, 0);
			if (borderGap) {
				g.setColor(color);
				g.drawLine((int) d, i, 0, i);
				g.drawLine((int) d + w1, i, w1, i);
			}
		}

	}

	private static void shearY(Graphics g, int w1, int h1, Color color) {

		int period = random.nextInt(40) + 10; // 50;

		boolean borderGap = true;
		int frames = 20;
		int phase = 7;
		for (int i = 0; i < w1; i++) {
			double d = (double) (period >> 1)
					* Math.sin((double) i / (double) period
					+ (6.2831853071795862D * (double) phase)
					/ (double) frames);
			g.copyArea(i, 0, 1, h1, 0, (int) d);
			if (borderGap) {
				g.setColor(color);
				g.drawLine(i, (int) d, i, 0);
				g.drawLine(i, (int) d + h1, i, h1);
			}

		}

	}
}