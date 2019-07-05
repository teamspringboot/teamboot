package com.advert.util;

import java.text.DateFormat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class SerializerGson extends JsonFactoryUtil{
	
	private static SerializerGson body = new SerializerGson();
	
	private SerializerGson() {

	}

	@Override
	public Gson buildGson(boolean flag,boolean existDate) {
		GsonBuilder gBuilder =  new GsonBuilder();
		if(existDate){
			gBuilder.registerTypeAdapter(java.util.Date.class, new JsonUtil.SerializerDateUtils()).setDateFormat(DateFormat.LONG);
			//.registerTypeAdapter(Collection.class, new JsonUtil.SerializerDateUtils());
		}
		if(flag){
			gBuilder.excludeFieldsWithoutExposeAnnotation();
		}
		Gson gson = gBuilder.create();
		return gson;
	}
	
	public static SerializerGson getInstance() {
		return body;
	}

}
