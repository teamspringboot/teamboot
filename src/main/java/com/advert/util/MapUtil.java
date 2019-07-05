package com.advert.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author tenderlitch
 */
public class MapUtil {

    /**
     * 将value添加到一个根据group类型分组的map的List集合中
     * @param map 分组map
     * @param group value对应的分组map的key
     * @param value 要添加到map中的值
     * @param <G> 分组类型
     * @param <V> 值类型
     */
    public static <G,V> void mapGroup(Map<G, List<V>> map, G group, V value){
        if(map==null) return;
        List<V> list=map.get(group);
        if(list==null){
            list=new ArrayList<>();
            map.put(group, list);
        }
        list.add(value);
    }
}
