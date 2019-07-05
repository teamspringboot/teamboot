package com.advert.util;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.advert.mapper.CustomObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * JSON 工具类
 *
 * @author 
 * @since 1.0.0
 */
public final class JsonUtil {
	private static ObjectMapper objectMapper = new CustomObjectMapper("");
	/**
	 * json转化成对象
	 * @param <T>
	 * @param jsonStr
	 * @param flag 是否限制属性输出，得到需要的对象，如果需要，加上@Expose在BEAN属性上，不需要传入FLASE。
	 * @param classType需要得到的对象
	 * @param existDate对象中是否有DATE类型，如有，需要反序列化，传入TRUE即可
	 * @return
	 */
	public static <T> T jsonToBean(String jsonStr,boolean flag,Class<T> classType,boolean existDate){
		Gson gson = JsonFactoryUtil.getInstanceFactory(JsonFactoryUtil.deserializer).buildGson(flag, existDate);
		T obj = gson.fromJson(jsonStr, classType);
		return obj;
	}
	
	/**
	 * bean转化为JSON
	 * @param <T>
	 * @param obj	需要转为JSON的对象
	 * @param flag 是否限制属性输出，得到需要的对象，如果需要，加上@Expose在BEAN属性上，不需要传入FLASE。
	 * @param existDate 对象中是否有DATE类型，如有，需要反序列化，传入TRUE即可
	 * @return
	 */
	public static <T> String beanToJson(T obj,boolean flag,boolean existDate){
		Gson gson = JsonFactoryUtil.getInstanceFactory(JsonFactoryUtil.serializer).buildGson(flag, existDate);
		String jsonStr = gson.toJson(obj);
		return jsonStr;
	}
	
	/**
	 * list转化为JSON
	 * @param <T>
	 * @param list
	 * @param flag
	 * @param existDate
	 * @param listType
	 * @return
	 */
	public static <T> String listToJson(List<T> list,boolean flag,boolean existDate,Type listType){
		Gson gson = JsonFactoryUtil.getInstanceFactory(JsonFactoryUtil.serializer).buildGson(flag, existDate);
		String jsonStr = gson.toJson(list, listType);
		return jsonStr;
	}
	
	/**
	 * 
	 * @param <T>
	 * @param jsonStr
	 * @param flag
	 * @param existDate
	 * @param listType  传入需要的Type  例如：Type listType = new TypeToken<List<TestBean>>() {}.getType()
	 * @return
	 */
	public static <T> List<T> jsonToList(String jsonStr,boolean flag,boolean existDate,Type listType){
		Gson gson = JsonFactoryUtil.getInstanceFactory(JsonFactoryUtil.deserializer).buildGson(flag, existDate);
	    List<T> list = gson.fromJson(jsonStr, listType);
		return list;
	}
	
	/**
	 * map转化为JSON
	 * @param <K>
	 * @param <V>
	 * @param map
	 * @param flag
	 * @param existDate
	 * @param listType
	 * @return
	 */
	public static <K,V> String MapToJson(Map<K,V> map,boolean flag,boolean existDate,Type listType){
		Gson gson = JsonFactoryUtil.getInstanceFactory(JsonFactoryUtil.serializer).buildGson(flag, existDate);
		String jsonStr = gson.toJson(map, listType);
		return jsonStr;
	}
	
	/**
	 * JSON转化为MAP
	 * @param <K>
	 * @param <V>
	 * @param jsonStr
	 * @param flag
	 * @param existDate
	 * @param listType   -----此处为HASHMAP.class的CLASS
	 * @return
	 */
	public static <K,V> Map<K,V> jsonToMap(String jsonStr,boolean flag,boolean existDate,Type listType){
		Gson gson = JsonFactoryUtil.getInstanceFactory(JsonFactoryUtil.deserializer).buildGson(flag, existDate);
	    Map<K,V> map = gson.fromJson(jsonStr, listType);
		return map;
	}
	
	public static class DeserializerDateUtils implements JsonDeserializer<java.util.Date>{

	    @Override

	    public java.util.Date deserialize(JsonElement json, Type type,

	           JsonDeserializationContext context) throws JsonParseException {

	       return new java.util.Date(json.getAsJsonPrimitive().getAsLong());

	    }
	}
	
	public static class SerializerDateUtils implements JsonSerializer<java.util.Date>{

	    @Override

	    public JsonElement serialize(Date date, Type type,

	           JsonSerializationContext content) {

	       return new JsonPrimitive(date.getTime());

	    }

	 

	}
    
    /**
     * 对json字符串格式化输出
     * @param jsonStr
     * @return
     */
    public static String formatJson(String jsonStr) {
        if (null == jsonStr || "".equals(jsonStr)) return "";
        StringBuilder sb = new StringBuilder();
        char last = '\0';
        char current = '\0';
        int indent = 0;
        for (int i = 0; i < jsonStr.length(); i++) {
            last = current;
            current = jsonStr.charAt(i);
            switch (current) {
                case '{':
                case '[':
                    sb.append(current);
                    sb.append('\n');
                    indent++;
                    addIndentBlank(sb, indent);
                    break;
                case '}':
                case ']':
                    sb.append('\n');
                    indent--;
                    addIndentBlank(sb, indent);
                    sb.append(current);
                    break;
                case ',':
                    sb.append(current);
                    if (last != '\\') {
                        sb.append('\n');
                        addIndentBlank(sb, indent);
                    }
                    break;
                default:
                    sb.append(current);
            }
        }

        return sb.toString();
    }

    /**
     * 添加space
     * @param sb
     * @param indent
     */
    private static void addIndentBlank(StringBuilder sb, int indent) {
        for (int i = 0; i < indent; i++) {
            sb.append('\t');
        }
    }
    /**
     * 将 JSON 字符串转为 POJO 对象
     */
    public static <T> T fromJson(String json, Class<T> type) {
        T pojo;
        try {
            pojo = objectMapper.readValue(json, type);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return pojo;
    }
}
