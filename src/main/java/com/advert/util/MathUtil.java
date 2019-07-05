/*---------------------------------------------------------------
 * Confidential and Proprietary
 * Copyright 2006 CMO & Hewlett-Packard Development Company, L.P.
 * All Rights Reserved
 * 
 * Project Name    : MPS
 * Sub Project Name: MPS
 *
 * File Name       : MathUtil.java
 * Created By      : huanghho
 * Created Date    : 2006-6-5      
 *---------------------------------------------------------------
 */
package com.advert.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

/**
 * MathUtil is a collection of math util functions
 * @version     1.0    
 * @author      huanghho    
 */
public class MathUtil {
    private static final BigInteger[] factorials = new BigInteger[100];
    
    public static BigInteger factorial(int n) {
        if(n < 100 && factorials[n] != null){
            return factorials[n];
        }

        BigInteger f = new BigInteger(String.valueOf(1));
        factorials[1] = new BigInteger(String.valueOf(1));
        for(int i = 2; i <= n; i++){
        	 	f = f.multiply(new BigInteger(String.valueOf(i)));
	            if(i < 100){
	                factorials[i] = f;
	            }
        }
        return f;
    }
    
    public static long combin(int n, int k){
    	
    	BigInteger temp = factorial(k).multiply(factorial(n - k));
        return factorial(n).divide(temp).longValue();
    }
    
    public static double round(double d, int n, int roundingMode){
        BigDecimal bd = new BigDecimal(Double.toString(d));
        bd = bd.setScale(n, roundingMode);
        return bd.doubleValue();
    }
    
    /**
     * converts the specified <code>val</code> to the base36 value.
     * note:the largest value is "9Z", and the smallest value is "0A".
     * if <code>val</code> is <code>null</code> or empty, returns the 
     * smallest value. if <code>val</code> is the largest value, throws
     * the <code>ArithmeticException</code>.
     * @param val to be converted value, a value of base36
     * @return a <code>String</code> after increase by 1 based 36
     * @author sunf
     * @since AP.308
     */
    public static String increaseByBase36(String val) {
    	return Base36.increase(val);
    }
    
    /**
     * converts the specified <code>val</code> to the base36 value.
     * note:the largest value is "*Z", and the smallest value is "*1",
     * the * range is 0-Z.
     * if <code>val</code> is <code>null</code> or empty, returns the 
     * smallest value. if <code>val</code> is the largest value, throws
     * the <code>ArithmeticException</code>.
     * @param val to be converted value, a value of base36
     * @return a <code>String</code> after increase by 1 based 36
     * @author weiz
     * @since AP.131
     */
    public static String increaseByBase(String val) {
        return Base36.increaseBase(val);
    }
    
    /**
     * converts the val to the new value by 10 increased.
     * for. if val=00, the converted value should be 10,
     * val=90, and the new value should be A0.
     *  the second value scope is 0~Z, and the first number
     *  keeps unchanged.
     * @param val to be converted value
     * @return the converted value
     * @author sunf
     * @since AP.125 Import imitial WIP/Bank
     */
    public static String increaseBy10(String val) {
    	return Base36.increaseBy10(val);
    }
    
    /**
     * converts the val to the new value by 1 increased.
     * for. if val=00, the converted value should be 01,
     * val=09, and the new value should be 10.
     *  the value scope is 01~99.
     * @param val to be converted value
     * @return the converted value
     * @author sunf
     * @since for generating the version id of MpsPlanVer
     */
    public static String increaseBy1(String val) {
    	return Base36.increaseBy1(val);
    }
    
    /**
     * converts the val to the new value by 1 increased.
     * for. if val=000, the converted value should be 001,
     * val=009, and the new value should be 010.
     *  the value scope is 001~999.
     * @param val to be converted value
     * @return the converted value
     * @throws ArithmeticException if does not match the rule
     * @author sunf
     * @since for generating the version id of MpsPlanVer
     */
    public static String increaseBy1To999(String val) {
    	return Base36.increaseBy1To999(val);
    }
    
    
    private static class Base36 {
    	  private static final char [] BASE= {'0', '1', '2', '3', '4', '5',
                '6', '7', '8', '9', 'A', 'B',
                'C', 'D', 'E', 'F', 'G', 'H',
                'I', 'J', 'K', 'L', 'M', 'N',
                'O', 'P', 'Q', 'R', 'S', 'T',
                'U', 'V', 'W', 'X', 'Y', 'Z'};
    	  
    	  private static final char STEP=1;
    	  public static final String SMALLEST_VALUE="0A";
    	  public static final String LARGEST_VALUE="9Z";
    	  
    	  private static final String SMALLEST_VALUE_10="00";
    	  private static final String LARGEST_VALUE_10="Z0";
    	  
    	  private static final String SMALLEST_VALUE_1="01";
    	  private static final String LARGEST_VALUE_1="99";    	  
    	 
          private static final String LARGEST_VALUE_XX = "ZZ";         
    	  
    	  public static String increase(final String val) {
    	  	String newVal=val==null?null:val.toUpperCase();
    	  	if(newVal==null || newVal.length()==0) {
        		return Base36.SMALLEST_VALUE;
        	} else if (newVal.length()>2 || newVal.compareTo(Base36.LARGEST_VALUE)>=0) {
        		throw new ArithmeticException("invalid value or reach the largest value: " + val);
        	} else if(newVal.length()==1) {
        		newVal=BASE[0] +newVal;
        	} 
        	char [] tmp= {newVal.charAt(0),newVal.charAt(1)};
        	if(tmp[1]<BASE[10] || tmp[0]>BASE[9]) {
        		throw new IllegalArgumentException("incorrect value: " + val);
        	}
        	tmp[1]+=STEP;
        	if(tmp[1]>BASE[BASE.length-1]) {
        		tmp[0]+=STEP;
        		tmp[1]=BASE[10];
        	}
        	return String.valueOf(tmp);
    	  }
          
    	  public static String increaseBase(final String val) {
              String newVal = val == null ? null : val.toUpperCase();
              if(newVal==null || newVal.length()==0) {
                  return SMALLEST_VALUE_1;
              } else if(newVal.length()>2 || newVal.compareTo(LARGEST_VALUE_XX)>=0) {
                  throw new ArithmeticException("invalid value or reach the largest value: " + val);
              } else if(newVal.length()==1) {
                  newVal=BASE[0] + newVal;
              }
              char [] tmp={newVal.charAt(0), newVal.charAt(1)};
              if(tmp[1]>BASE[BASE.length-1] || tmp[1]<BASE[0]) {
                  throw new IllegalArgumentException("incorrect value: " + val);
              }
              tmp[1] = BASE[Arrays.binarySearch(BASE, tmp[1]) + + STEP];
              return String.valueOf(tmp);
          }
          
    	  public static String increaseBy10(final String val) {
    		  String newVal=val==null?null:val.toUpperCase();
    		  if(newVal==null || newVal.length()==0) {
    			  return SMALLEST_VALUE_10;
    		  } else if(newVal.length()>2 || newVal.compareTo(LARGEST_VALUE_10)>=0) {
    			  throw new ArithmeticException("invalid value or reach the largest value: " + val);
    		  } else if(newVal.length()==1) {
    			  newVal=BASE[0] + newVal;
    		  }
    		  char [] tmp={newVal.charAt(0), BASE[0]};
    		  
    		  if(tmp[0]>BASE[BASE.length-1] || tmp[0]<BASE[0] 
    		         || tmp[1]>BASE[9] || tmp[1]<BASE[0]) {
    			  throw new IllegalArgumentException("incorrect value: " + val);
    		  }
    		  tmp[0]=BASE[Arrays.binarySearch(BASE, tmp[0]) + STEP];
    		  return String.valueOf(tmp);
    	  }
    	  
    	  public static String increaseBy1(final String val) {
    		  String newVal=val==null?null:val.toUpperCase();
    		  if(newVal==null || newVal.length()==0) {
    			  return SMALLEST_VALUE_1;
    		  } else if(newVal.length()>2 || newVal.compareTo(LARGEST_VALUE_1)>=0) {
    			  throw new ArithmeticException("invalid value or reach the largest value: " + val);
    		  } else if(newVal.length()==1) {
    			  newVal=BASE[0] + newVal;
    		  }
    		  char [] tmp={newVal.charAt(0), newVal.charAt(1)};
    		  
    		  if(tmp[0]>BASE[9] || tmp[0]<BASE[0] 
    		         || tmp[1]>BASE[9] || tmp[1]<BASE[0]) {
    			  throw new IllegalArgumentException("incorrect value: " + val);
    		  }
    		  int nextVal =Integer.parseInt(newVal);
    		  
    		  nextVal=nextVal +1; 
    		  
    		  if(nextVal<10) {
    			  newVal=String.valueOf(BASE[0]) + nextVal;
    		  } else {
    			  newVal=String.valueOf(nextVal);
    		  }
    		  return newVal;
    	  } 
    	  
    	  /*
    	   * 001~999
    	   */
    	  public static String increaseBy1To999(final String val) {
    		  final int MAX_LENGTH=3;
    		  final int LARGEST_VALUE_001=999;
    		  boolean isDigit=NumberUtils.isDigits(val);
    		  if(! isDigit && val !=null && val .length()>0) {
    			  throw new IllegalArgumentException("not a digit type value." + val);    			  
    		  }
    		  
    		  int nextVal=NumberUtils.toInt(val) +1;
    		  if(nextVal > LARGEST_VALUE_001) {
    			  throw new ArithmeticException("invalid value or reach the largest value: " + val);
    		  }
    		  String nextValAsStr=String.valueOf(nextVal);
    		  nextValAsStr=StringUtils.leftPad(nextValAsStr, MAX_LENGTH, BASE[0]);

    		  return nextValAsStr;
    	  } 
    }
    
	/**
	 * Set the scale of the double value, and round the result.
	 * 
	 * <pre>
	 * setValueScale(0.124456, 2) = 0.12;
	 * setValueScale(0.125456, 2) = 0.13;
	 * setValueScale(0.125456, 3) = 0.125;
	 * </pre>
	 * 
	 * @param value the value to be set
	 * @param scale the scale for the returned result, eg: 2
	 * @return the value rounded according to the scale
	 * 
	 * @author chenke
	 */
	public static double setValueScale(double value, int scale) {
		
		/*
		 * Implement this method using BigDecimal#setScale would be 
		 * more than 40 times slower.
		 */
		double factor = Math.pow(10.0, scale);
		return Math.round(value * factor) / factor;
	}
	
	public static double setValueScale(double value) {
		return setValueScale(value, 2);
	}
	
	// 默认除法运算精度
	private static final int DEF_DIV_SCALE = 10;

	/**
	 * 提供精确的加法运算
	 * 
	 * @param v1
	 *            被加
	 * @param v2
	 *            加数
	 * @return 两个参数的和
	 */
	public static double plus(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.add(b2).doubleValue();
	}

	/**
	 * 提供精确的减法运算
	 * 
	 * @param v1
	 *            被减
	 * @param v2
	 *            减数
	 * @return 两个参数的差
	 */
	public static double minus(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.subtract(b2).doubleValue();
	}

	/**
	 * 提供精确的乘法运算
	 * 
	 * @param v1
	 *            被乘
	 * @param v2
	 *            乘数
	 * @return 两个参数的积
	 */
	public static double multiply(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.multiply(b2).doubleValue();
	}

	/**
	 * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到 小数点以10位，以后的数字四舍五入
	 * 
	 * @param v1
	 *            被除
	 * @param v2
	 *            除数
	 * @return 两个参数的商
	 */
	public static double div(double v1, double v2) {
		return div(v1, v2, DEF_DIV_SCALE);
	}

	/**
	 * 提供（相对）精确的除法运算当发生除不尽的情况时，由scale参数定精度，以后的数字四舍五入
	 * 
	 * @param v1
	 *            被除
	 * @param v2
	 *            除数
	 * @param scale
	 *            表示表示要精确到小数点以后几位
	 * @return 两个参数的商
	 */
	public static double div(double v1, double v2, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero");
		}
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/**
	 * 提供精确的小数位四舍五入处理
	 * 
	 * @param v
	 *            要四舍五入的数字
	 * @param scale
	 *            小数点后保留几位
	 * @return 四舍五入后的结果
	 */
	public static double round(double v, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero");
		}
		BigDecimal b = new BigDecimal(Double.toString(v));
		BigDecimal one = new BigDecimal("1");
		return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/*	public static float add(float v1, float v2) {
			BigDecimal b1 = new BigDecimal(Float.toString(v1));
			BigDecimal b2 = new BigDecimal(Float.toString(v2));
			float result=b1.add(b2).floatValue();
			return result;
		}

		public static float sub(float v1, float v2) {
			BigDecimal b1 = new BigDecimal(Float.toString(v1));
			BigDecimal b2 = new BigDecimal(Float.toString(v2));
			float result=b1.subtract(b2).floatValue();
			return result;
		}

		public static float mul(float v1, float v2) {
			BigDecimal b1 = new BigDecimal(Float.toString(v1));
			BigDecimal b2 = new BigDecimal(Float.toString(v2));
			return b1.multiply(b2).floatValue();
		}

		public static float div(float v1, float v2) {
			BigDecimal b1 = new BigDecimal(Float.toString(v1));
			BigDecimal b2 = new BigDecimal(Float.toString(v2));
			return b1.divide(b2, 3, BigDecimal.ROUND_HALF_UP).floatValue();
		}

		public static float div(float v1, float v2, int scale) {
			if (scale < 0) {
				throw new IllegalArgumentException(
						"The scale must be a positive integer or zero");
			}
			BigDecimal b1 = new BigDecimal(Float.toString(v1));
			BigDecimal b2 = new BigDecimal(Float.toString(v2));
			return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).floatValue();
		}

		public static float round(float v, int scale) {
			if (scale < 0) {
				throw new IllegalArgumentException(
						"The scale must be a positive integer or zero");
			}
			BigDecimal b = new BigDecimal(Float.toString(v));
			BigDecimal one = new BigDecimal("1");
			return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).floatValue();
		}*/
    
}
