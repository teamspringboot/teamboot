package com.advert.util;

import java.text.DateFormat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class DeserializerGson extends JsonFactoryUtil{
	
	private static final DeserializerGson body = new DeserializerGson();
	
	private DeserializerGson() {

	}

	@Override
	public Gson buildGson(boolean flag, boolean existDate) {
		GsonBuilder gBuilder =  new GsonBuilder();
		if(existDate){
			gBuilder.registerTypeAdapter(java.util.Date.class, new JsonUtil.DeserializerDateUtils()).setDateFormat(DateFormat.LONG)
			//.registerTypeAdapter(Collection.class, typeAdapter)
			;
			
		}
		if(flag){
			gBuilder.excludeFieldsWithoutExposeAnnotation();
		}
	    Gson gson = gBuilder.create();
	    return gson;
	}
	
	public static DeserializerGson getInstance() {
		return body;
	}

}
