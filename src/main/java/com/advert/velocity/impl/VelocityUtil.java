package com.advert.velocity.impl;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import com.advert.util.MathUtil;
import com.advert.util.StringUtil;




/**
 * ---------------------------------------------------------------------------------
 Confidential and Proprietary                                                                
 Copyright 2008 By                                                                                     
 SGAI & Hewlett-Packard Development Company, L.P.                 
 All Rights Reserved                                                                                  

 Project Name : SGAI  MES                                                                                                                                       
 Class Name   : VelocityUtil.java    
 Package      : com.hp.common.velocity.service.impl                                                                   
 @version     $Id$                                                          
 @author yehuo
 @since  Feb 23, 2009 
 */

public class VelocityUtil {
    private static final String DELIM = ",";
    private static final String INDELIM = ":";
    private static final DecimalFormat format = new DecimalFormat("0.000");

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
        return MathUtil.div(v1, v2);
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
        return MathUtil.div(v1, v2, scale);
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
        return MathUtil.minus(v1, v2);
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
    public static double multiply(Object v1, Object v2) {
        if(null==v1||null==v2){
            return 0;
        }
        try {
            double tem1 = Double.valueOf(v1.toString()).doubleValue();
            double tem2 = Double.valueOf(v2.toString()).doubleValue();
            return Double.valueOf(format.format(MathUtil.multiply(tem1, tem2)));
        } catch (Exception e) {
            return 0;
        } catch (Error ex) {
            return 0;
        }
    }

    /**
     * 提供精确的乘法运算
     * 
     * @param v1
     *            被乘
     * @param 10E power
     *          
     * @return 两个参数的积
     */
    public static double power(Object v1, int power) {
        if(null==v1){
            return 0;
        }
        try {
            double result =Double.valueOf(v1.toString());
            if(power<0){
                for (int i = 0; i < -power; i++)
                    result = result / 10; 
            }else if(power>0){
                for (int i = 0; i < power; i++)
                    result = result * 10;
            }else{
                result = 1;
            }
            return Double.valueOf(format.format(result)).doubleValue();
        } catch (Error ex) {
            return 0;
        } catch (NumberFormatException e) {
            return 0;
        }
    }

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
        return MathUtil.plus(v1, v2);
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
    public static double round(Object v, int scale) {
        if(null==v){
            return 0;
        }
        try{
            return MathUtil.round(Double.valueOf(v.toString()).doubleValue(), scale);
        }catch(Exception e){
            return 0;
        }catch(Error er){
            return 0;
        }     
    }

    public static double round(double d, int n, int roundingMode) {
        return MathUtil.round(d, n, roundingMode);
    }
    
    /**
     * 解码
     * 
     * @param key 源码
     * @param map 源码目标码映射字符串 ex("1:2,2:2")
     * @param def 解码失败的默认码
     * @return 目标码
     */
    public static String decode(String key, String map,String def) {
        //源、目标码对应字典为空的逻辑
        if (StringUtil.isBlank(map)) {
            if(StringUtil.isBlank(def)){
                return key;
            }else{
                return def;
            }
        }
        
        //字码集从String转换成Map
        map = map.trim();
        StringTokenizer strTok = new StringTokenizer(map, DELIM, false);
        Map<String, String> tureMap = new HashMap<String, String>();
        while (strTok.hasMoreTokens()) {
            String s = strTok.nextToken();
            String[] sa = s.split(INDELIM);
            if (null == sa || sa.length != 2) {
                continue;
            } else {
                tureMap.put(sa[0].trim(), sa[1].trim());
            }
        }
        
        //解码逻辑
        if(null!=key){
            key = key.trim();
        }
        String code =tureMap.get(key);
        if(StringUtil.isBlank(code)){
            if(StringUtil.isBlank(def)){
                return key;
            }else{
                return def;
            }
        }

        return code;
    }
    
    /**
     * 解码
     * 
     * @param key 源码
     * @param map 源码目标码映射字符串 ex("1:2,2:2")
     * @return 目标码
     */
    public static String decode(String key, String map) {
        return decode(key, map,null);
    }
}
