/*
 *  Confidential and Proprietary                                                                
 Copyright 2008 By                                                                                     
 SGAI & Hewlett-Packard Development Company, L.P.                 
 All Rights Reserved                                                                                  

 Project Name : SGAI  MES                                                                                                                                       
 Class Name   : RangeTypeComparator.java    
 Package      : com.hp.common.util    
 */
package com.advert.util;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

/**
 * defines a range type comparator.
 @version     $Id$                                                          
 @author sunf
 @since  2008-11-18 
 */
public class RangeTypeComparator {
    private static Map<String,Integer> rangeMap = new HashMap<String,Integer>() ;
    public static Map<String,Integer> getRangeMap(){
        if(rangeMap.size()==0){
            rangeMap.put("=", Integer.valueOf(1)) ;
            rangeMap.put("[)", Integer.valueOf(2)) ;
            rangeMap.put("[]", Integer.valueOf(3)) ;
            rangeMap.put("()", Integer.valueOf(4)) ;
            rangeMap.put("(]", Integer.valueOf(5)) ;
            rangeMap.put(")", Integer.valueOf(6)) ;
            rangeMap.put("]", Integer.valueOf(7)) ;
            rangeMap.put("(", Integer.valueOf(8)) ;
            rangeMap.put("[", Integer.valueOf(9)) ;
        }
        return rangeMap ;
    } 
    
    
    public static enum RangeType {
        EQ (1, Operator.EQ, Operator.NULL) {
            
            @Override
            public boolean isInside(double lowerValue, double upperValue, double val) {
                return lowerValue==val;
            }           
        }, 
            
        GELT(2, Operator.GE, Operator.LT) {

            @Override
            public boolean isInside(double lowerValue, double upperValue, double val) {
                return val>=lowerValue && val<upperValue;
            }           
        },
        
        GELE(3, Operator.GE, Operator.LE) {

            @Override
            public boolean isInside(double lowerValue, double upperValue, double val) {
                return val>=lowerValue && val<=upperValue;
            }           
        },
        
        GTLT(4, Operator.GT, Operator.LT) {

            @Override
            public boolean isInside(double lowerValue, double upperValue, double val) {
                return val>lowerValue && val<upperValue;
            }           
        },
        
        GTLE(5, Operator.GT, Operator.LE) {

            @Override
            public boolean isInside(double lowerValue, double upperValue, double val) {
                return val>lowerValue && val<=upperValue;
            }           
        },
        
        LT(6, Operator.NULL, Operator.LT) {

            @Override
            public boolean isInside(double lowerValue, double upperValue, double val) {
                return val<upperValue;
            }           
        },
        
        LE(7, Operator.NULL, Operator.LE) {

            @Override
            public boolean isInside(double lowerValue, double upperValue, double val) {
                return val<=upperValue;
            }           
        },
        
        GT(8, Operator.GT, Operator.NULL) {

            @Override
            public boolean isInside(double lowerValue, double upperValue, double val) {
                return val>lowerValue;
            }           
        },
        
        GE(9, Operator.GE, Operator.NULL) {

            @Override
            public boolean isInside(double lowerValue, double upperValue, double val) {
                return val>=lowerValue;
            }           
        };
        
        private final int code; 
        private final Operator lower;
        private final Operator upper;
        
        RangeType(int code, Operator lower, Operator upper) {
            this.code=code;
            this.lower=lower;
            this.upper=upper;
        }
        
        public Operator lowerOperator() {
            return this.lower;
        }
        
        public Operator upperOperator() {
            return this.upper;
        }
        
        public int code() {
            return this.code;
        }
        
        public String alias() {
            if(this.upper!=null) {
                if(this.lower !=null) {
                    return this.lower.boundary + "," + this.upper.boundary;
                } else {
                    return this.upper.boundary;
                }               
            } else {
                if(this.lower !=null) {
                    return this.lower.boundary;
                } else {
                    return "";
                }               
            }
        }
        
        public abstract boolean isInside(double lowerValue, double upperValue, double val);
        
        public RangeType merge(RangeType other) {
            boolean compare=this.lower.getPriority()<other.lower.getPriority();
            Operator operLeft=compare==true?other.lower:this.lower;
            compare=this.upper.getPriority()<other.upper.getPriority();
            Operator operRight=compare==true?other.upper:this.upper;
            
            return operatorType.get(operLeft).get(operRight);
        }

    }
    
    public static enum Operator {
        EQ("Equal", "=", "=") {
            @Override
            public int getPriority() {
                return 0;
            }           
        },
        
        NE("NotEqual", "<>", "!=") {
            @Override
            public int getPriority() {
                return 1;
            }           
        },
        
        GE("GreaterEqual", ">=", "[") {
            @Override
            public int getPriority() {              
                return 2;
            }   
        },      
        
        GT("GreaterThan", ">", "(") {
            @Override
            public int getPriority() {              
                return 3;
            }   
        },
        
        LE("LowerEqual", "<=", "]") {
            @Override
            public int getPriority() {              
                return 4;
            }   
        },
        
        LT("LowerThan", "<", ")"){
            @Override
            public int getPriority() {              
                return 5;
            }           
        }, 
        NULL("NULL", "", ""){
            @Override
            public int getPriority() {              
                return -1;
            }           
        };
        
        private final String alias;
        private final String expression; 
        private final String boundary;
        
        Operator(String alias, String exp, String boundary) {
            this.alias=alias;
            this.expression=exp;
            this.boundary=boundary;
        }
        
        public String alias() {
            return this.alias;
        }
        
        public String expression() {
            return this.expression;
        }
        
        public String boundary() {
            return this.boundary;
        }
        
        public abstract int getPriority();
    }
    
    private static Map<Integer, RangeType> rangeTypeMap=new HashMap<Integer, RangeType>();
    
    private static Map<Operator, Map<Operator, RangeType>> operatorType
        =new EnumMap<Operator, Map<Operator, RangeType>>(Operator.class);
    
    static {
        for(RangeType rangeType:RangeType.values()) {
            rangeTypeMap.put(rangeType.code, rangeType);
            Map<Operator, RangeType> upperOperatorMap=operatorType.get(rangeType.lower);
            if(upperOperatorMap==null) {
                upperOperatorMap=new EnumMap<Operator, RangeType>(Operator.class);
                operatorType.put(rangeType.lower, upperOperatorMap);
            }           
            upperOperatorMap.put(rangeType.upper, rangeType);           
        }
    }
    
    public static class Range {
        private RangeType rangeType;
        private Double lowerValue;
        private Double upperValue;
        
        private Double targetValue ;
        
        // Because the overload is ambiguous
//      public Range(int rangeTypeCode, double lowerValue, double upperValue) {
//          this(getRangeType(rangeTypeCode), lowerValue, upperValue);
//      }
        
        public Range(int rangeTypeCode, Double lowerValue, Double upperValue) {
            this(getRangeType(rangeTypeCode),
                    lowerValue==null?Double.NaN : lowerValue.doubleValue(),
                            upperValue==null?Double.NaN : upperValue.doubleValue());
        }
        
        public Range(int rangeTypeCode, Double lowerValue, Double upperValue,Double targetValue) {
        	this(getRangeType(rangeTypeCode),
                    lowerValue==null?Double.NaN : lowerValue.doubleValue(),
                            upperValue==null?Double.NaN : upperValue.doubleValue(),
                            		targetValue==null?Double.NaN:targetValue.doubleValue()) ;
        }
        
        // Because the overload is ambiguous
//      public Range(RangeType rangeType, double lowerValue, double upperValue) {
//          this.rangeType=rangeType;
//          this.lowerValue=lowerValue;
//          this.upperValue=upperValue;
//      }
        
        public Range(RangeType rangeType, Double lowerValue, Double upperValue) {
            this.rangeType=rangeType;
            this.lowerValue=lowerValue==null?Double.NaN : lowerValue.doubleValue();
            this.upperValue=upperValue==null?Double.NaN : upperValue.doubleValue();
        }
        
        public Range(RangeType rangeType, Double lowerValue, Double upperValue,Double targetValue) {
            this.rangeType=rangeType;
            this.lowerValue=lowerValue==null?Double.NaN: lowerValue.doubleValue();
            this.upperValue=upperValue==null?Double.NaN: upperValue.doubleValue();
            this.targetValue=targetValue==null?Double.NaN:targetValue.doubleValue() ;
        }
        
        public Range merge(Range other) {
        	if(Double.isNaN(this.lowerValue))
        		this.lowerValue = 0d ;
        	
        	if(Double.isNaN(this.upperValue))
        		this.upperValue = 999d ;
        	
        	if(Double.isNaN(other.lowerValue))
        		other.lowerValue = 0d ;
        	
        	if(Double.isNaN(other.upperValue)){
        		other.upperValue = 999d ;
        	}
        	
            boolean compare=this.lowerValue<other.lowerValue;
            double left= (compare?other.lowerValue:this.lowerValue) ;
            Operator operLeft= (compare?other.rangeType.lower:this.rangeType.lower);
            if(this.lowerValue==other.lowerValue){
                compare=this.rangeType.lower.getPriority()<other.rangeType.lower.getPriority();
                operLeft= (compare?other.rangeType.lower:this.rangeType.lower) ;
            }
            
            compare=this.upperValue<other.upperValue;
            double right=compare?this.upperValue:other.upperValue;
            Operator operRight=compare==true?this.rangeType.upper:other.rangeType.upper;
            if(this.upperValue==other.upperValue){
                compare=this.rangeType.upper.getPriority()<other.rangeType.upper.getPriority();
                operRight=compare==true?other.rangeType.upper:this.rangeType.upper;
            }
            
            RangeType mergedRangeType= operatorType.get(operLeft).get(operRight);
            
            return new Range(mergedRangeType.code, left, right);
        }
        
        public boolean isValidRange() {
            
            if( Double.isNaN(this.upperValue)  && !Double.isNaN(this.lowerValue) ){
                return true ;
            }
            
            if(this.upperValue>this.lowerValue){
                return true;
            }else if(this.upperValue==this.lowerValue){
                return this.rangeType.equals(RangeType.GELE);
            }else{
                return false;
            }
        }
        
        public String getBoundary() {
//          if(this.rangeType.upper.equals(Operator.NULL)) {
//              if(this.rangeType.lower.equals(Operator.NULL)) {
//                  return new StringBuilder().append(this.rangeType.lower.boundary)
//                  .append(this.lowerValue).append(", ").append(this.upperValue).append(this.rangeType.upper.boundary).toString();
//              } else {
//                  return null;
//              }               
//          } else {
//              if(this.rangeType.lower.equals(Operator.NULL)) {
//                  return null;
//              } else {
//                  return null;
//              }               
//          }
            
            if(RangeType.EQ.equals(this.rangeType)){
            	if( this.targetValue!=null && !Double.isNaN(this.targetValue)){
            		return new StringBuilder().append(this.rangeType.lower.boundary).append(this.targetValue).toString() ;
            	}else if(this.lowerValue!=null && !Double.isNaN(this.lowerValue)){
            		return new StringBuilder().append(this.rangeType.lower.boundary).append(this.lowerValue).toString() ;
            	}else{
                    return "" ;
                }
            }else if((RangeType.GELT.equals(this.rangeType)&& this.upperValue.equals(Double.NaN )) || (RangeType.GTLT.equals(this.rangeType)&&this.upperValue.equals(Double.NaN ))){
              return new StringBuilder().append(this.rangeType.lower.boundary).append(this.lowerValue).append(",∞)").toString() ;  
            }else{              
                return new StringBuilder().append(this.rangeType.lower.boundary)
                .append(Double.isNaN(this.lowerValue)?"":this.lowerValue).append(",").append(Double.isNaN(this.upperValue)?"":this.upperValue).append(this.rangeType.upper.boundary).toString();
            }
        }

        public RangeType rangeType() {
            return rangeType;
        }

//        public double lowerValue() {
//            return lowerValue;
//        }
//
//        public double upperValue() {
//            return upperValue;
//        }
        
        public Double getLowerValue() {
            return Double.isNaN(lowerValue)?null : lowerValue;
        }
        
        public Double getUpperValue() {
            return Double.isNaN(upperValue)?null : upperValue;
        }
    }

    public static Range merge(Range left, Range right) {
        return left.merge(right);
    }
    
    // Because the overload is ambiguous
//  public static boolean isInside(int rangeTypeCode, double lowerValue, double upperValue, double val) {
//      return getRangeType(rangeTypeCode).isInside(lowerValue, upperValue, val);
//  }
    
    public static boolean isInside(int rangeTypeCode, Double lowerValue, Double upperValue, Double val) {
        return getRangeType(rangeTypeCode).isInside(
                lowerValue==null?Double.NaN : lowerValue.doubleValue(),
                        upperValue==null?Double.NaN : upperValue.doubleValue(),
                                val==null?Double.NaN : val.doubleValue());
    }
    
    public static boolean isInside(String rangeTypeStr, String lowerValue, String upperValue, String val) {
    	if(StringUtils.isBlank(rangeTypeStr)){
    		rangeTypeStr = "=" ; 
    	}
    	int rangeTypeCode = RangeTypeComparator.getRangeMap().get(rangeTypeStr) ;
        return getRangeType(rangeTypeCode).isInside(
                StringUtils.isEmpty(lowerValue)?Double.NaN : Double.valueOf(lowerValue) ,
                		StringUtils.isEmpty(upperValue)?Double.NaN : Double.valueOf(upperValue),
                				StringUtils.isEmpty(val)?Double.NaN : Double.valueOf(val));
    }
    
    public static boolean isInside(Range range, Double val) {
        return isInside(range.rangeType.code, range.lowerValue, range.upperValue, val==null?Double.NaN : val.doubleValue());
    }
    
    public static RangeType getRangeType(int rangeTypeCode) {       
        return rangeTypeMap.get(rangeTypeCode);
    }

    
	public static boolean isInside(Integer rangeType, Double lowerLimit,
			Double upperLimit, Double targetValue, Double actualValue) {
		
		boolean flag = false ;
		
		if(rangeType==1){
			flag = isInside(rangeType, targetValue,upperLimit,actualValue) ;
		}else{
			flag = isInside(rangeType, lowerLimit,upperLimit,actualValue) ;
		}
		
		return flag;
	}
	
	public static String getRangeText(Integer range, String lower,String upper , String targetValue){
		
		String result = null ;
		
		RangeType rangeType = getRangeType(range.intValue()) ;
		
		if(RangeType.EQ.equals(rangeType)){
//		   if(!StringUtil.isBlank(targetValue)){
//               result =  new StringBuilder().append(rangeType.lower.boundary).append(targetValue).toString() ;
//		   }
			result=targetValue;
        }else {              
            result =  new StringBuilder().append(rangeType.lower.boundary)
            .append(lower).append(",").append(upper).append(rangeType.upper.boundary).toString();
        }
				
		return StringUtils.remove(result==null?"":result, "null");
	}
    
	public static void main(String[] args) {
		System.out.println(getRangeText(3, "1", "3", "5")); 
	}
	
	public static String getRangeText(String range, String lower,String upper , String targetValue){
		
		String result = null ;	
		if(StringUtils.isEmpty(range) || "-".equals(range)){
			range="=";
		}
		
		RangeType rangeType = getRangeType(getRangeMap().get(range).intValue()) ;
		
		if(RangeType.EQ.equals(rangeType)){
			result=targetValue;
        }else {              
            result =  new StringBuilder().append(rangeType.lower.boundary)
            .append(lower).append(",").append(upper).append(rangeType.upper.boundary).toString();
        }              
        return StringUtils.remove(result==null?"":result, "null");
        
        
    }
    
    
}
