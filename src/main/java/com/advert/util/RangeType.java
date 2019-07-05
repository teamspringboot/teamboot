package com.advert.util;

/**
 * 
 * ---------------------------------------------------------------------------------
 Project Name : SGAI  MES                                                                                                                                       
 Class Name   : RangeType.java    
 Package      : com.sgai.core.util                                                                   
 @version     $Id$                                                          
 @author dongweizhen
 @since  2014-09-18
 */
public enum RangeType {
    /*
     * L:low,H:high,B:bigger,S:smaller,E:equal
     */
    LB_HS("(", ")"), // ()区间类型
    LE_HE("[", "]"), // []区间类型
    LE_HS("[", ")"), // [)区间类型
    LB_HE("(", "]"), // (]区间类型
    LN_HE("", "]"),  // -& ]区间类型
    LE_HN("[", ""),  // [ +&区间类型
    LN_HS("", ")"),  // -& )区间类型
    LB_HN("(", ""),  // ( +&区间类型
    HE("", "HV_EQ"), // HV_EQ等于高值
    LE("LV_EQ", ""); // LV_EQ等于低值

    private String low;
    private String high;

    RangeType(String low, String high) {
        this.low = low;
        this.high = high;
    }
    
    public String toString(){
        if(this.low!=null)
            this.low=low.trim();
        if(this.high!=null)
            this.high=high.trim();
        return this.low+this.high;
    }
    /**
     * @return the low
     */
    public String getLow() {
        return low;
    }

    /**
     * @param low the low to set
     */
    public void setLow(String low) {
        this.low = low;
    }

    /**
     * @return the high
     */
    public String getHigh() {
        return high;
    }

    /**
     * @param high the high to set
     */
    public void setHigh(String high) {
        this.high = high;
    }
}