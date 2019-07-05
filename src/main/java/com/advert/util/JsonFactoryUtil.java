package com.advert.util;

import com.google.gson.Gson;

public abstract  class JsonFactoryUtil {
	public static final String deserializer = "deserializer";
	public static final String serializer = "serializer";
	public abstract Gson buildGson(boolean flag,boolean existDate);
	public static JsonFactoryUtil getInstanceFactory(String which) {
		if(which.equals(JsonFactoryUtil.deserializer)){
			return DeserializerGson.getInstance();
		}else{
			return SerializerGson.getInstance();
		}
		
	}
}
