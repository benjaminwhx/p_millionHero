package com.github.hero.utils;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

import java.util.Date;

/**
 * @author 吴海旭
 * Date: 2017-12-12
 * Time: 下午6:03
 */
public class JSONUtils {

	public static JSONObject fromObject(Object object) {
		return JSONObject.fromObject(object, getJsonConfig());
	}

	public static JsonConfig getJsonConfig() {
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor());
		return jsonConfig;
	}
}

class JsonDateValueProcessor implements JsonValueProcessor {

	public JsonDateValueProcessor() {
		super();
	}

	@Override
	public Object processArrayValue(Object o, JsonConfig jsonConfig) {
		return process(o);
	}

	@Override
	public Object processObjectValue(String s, Object o, JsonConfig jsonConfig) {
		return process(o);
	}

	private Object process(Object value){
		if(value instanceof Date){
			return ((Date) value).getTime();
		}
		return value == null ? "" : value.toString();
	}
}