package com.example.hp.stepcount.Fall;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class DoubleUtil implements Serializable{
	
	private static final long serialVersionUID = -3345205828566485102L;
	// Ĭ�ϳ������㾫��
	private static final Integer DEF_DIV_SCALE = 2;

	/**
	 * �ṩ��ȷ�ļӷ����㡣
	 *
	 * @param value1
	 *            ������
	 * @param value2
	 *            ����
	 * @return ���������ĺ�
	 */
	public static Double add(Double value1, Double value2) {
		BigDecimal b1 = new BigDecimal(Double.toString(value1));
		BigDecimal b2 = new BigDecimal(Double.toString(value2));
		return b1.add(b2).doubleValue();
	}

	/**
	 * �ṩ��ȷ�ļ������㡣
	 *
	 * @param value1
	 *            ������
	 * @param value2
	 *            ����
	 * @return ���������Ĳ�
	 */
	public static double sub(Double value1, Double value2) {
		BigDecimal b1 = new BigDecimal(Double.toString(value1));
		BigDecimal b2 = new BigDecimal(Double.toString(value2));
		return b1.subtract(b2).doubleValue();
	}

	/**
	 * �ṩ��ȷ�ĳ˷����㡣
	 *
	 * @param value1
	 *            ������
	 * @param value2
	 *            ����
	 * @return ���������Ļ�
	 */
	public static Double mul(Double value1, Double value2) {
		BigDecimal b1 = new BigDecimal(Double.toString(value1));
		BigDecimal b2 = new BigDecimal(Double.toString(value2));
		return b1.multiply(b2).doubleValue();
	}

	/**
	 * �ṩ����ԣ���ȷ�ĳ������㣬�����������������ʱ�� ��ȷ��С�����Ժ�10λ���Ժ�������������롣
	 *
	 * @param dividend
	 *            ������
	 * @param divisor
	 *            ����
	 * @return ������������
	 */
	public static Double divide(Double dividend, Double divisor) {
		return divide(dividend, divisor, DEF_DIV_SCALE);
	}

	/**
	 * �ṩ����ԣ���ȷ�ĳ������㡣 �����������������ʱ����scale����ָ�����ȣ��Ժ�������������롣
	 *
	 * @param dividend
	 *            ������
	 * @param divisor
	 *            ����
	 * @param scale
	 *            ��ʾ��ʾ��Ҫ��ȷ��С�����Ժ�λ��
	 * @return ������������
	 */
	public static Double divide(Double dividend, Double divisor, Integer scale) {
		if (scale < 0) {
			throw new IllegalArgumentException("The scale must be a positive integer or zero");
		}
		BigDecimal b1 = new BigDecimal(Double.toString(dividend));
		BigDecimal b2 = new BigDecimal(Double.toString(divisor));
		return b1.divide(b2, scale, RoundingMode.HALF_UP).doubleValue();
	}

	/**
	 * �ṩָ����ֵ�ģ���ȷ��С��λ�������봦��
	 *
	 * @param value
	 *            ��Ҫ�������������
	 * @param scale
	 *            С���������λ
	 * @return ���������Ľ��
	 */
	public static double round(double value, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException("The scale must be a positive integer or zero");
		}
		BigDecimal b = new BigDecimal(Double.toString(value));
		BigDecimal one = new BigDecimal("1");
		return b.divide(one, scale, RoundingMode.HALF_UP).doubleValue();
	}
}
