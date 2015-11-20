package com.lettercity.gson;


import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.google.gson.Gson;

public class GsonTools {
	private final static String TAG = GsonTools.class.getSimpleName();
	
	public static String toJson(Object obj) {
		try {
			Gson gson = new Gson();
			return gson.toJson(obj);
		} catch (Exception e) {
			Log.w(TAG, "Exception in toJson from object", e);
		}
		return null;
	}
	
	public static <T> T toObject(String jsonString, Class<T> cls) {
		T t = null;
		try {
			Gson gson = new Gson();
			t = gson.fromJson(jsonString, cls);
		} catch (Exception e) {
			Log.w(TAG, "Exception in toObject with cls:" + cls.getSimpleName(), e);
		}
		return t;
	}
	

	public static <T> T toObject(JSONObject jsonObj, Class<T> cls) {
		return toObject(jsonObj.toString(), cls);
	}
	
//	public static <T> List<T> toObjectList(String jsonString, Class<T> cls) {
//		List<T> list = new ArrayList<T>();
//		try {
//			Gson gson = new Gson();
//			Type type = new TypeToken<List<T>>(){}.getType();
//			list =  gson.fromJson(jsonString, type);
//		} catch (Exception e) {
//			Log.w(TAG, "Exception in toObjectList with cls:" + cls.getSimpleName(), e);
//		}
//		return list;
//	}
	
	public static <T> List<T> toObjectList(JSONObject jsonObj, Class<T> cls) {
		return toObjectList(jsonObj.toString(), cls);
	}
	
	public static <T> List<T> toObjectList(String jsonString, Class<T> cls) {
		List<T> list = new ArrayList<T>();
		try {
			JSONArray jsonArray = new JSONArray(jsonString);
			Gson gson = new Gson();
			for (int i = 0, len = jsonArray.length(); i < len; i++) {
				T t = gson.fromJson(jsonArray.getString(i), cls);
				list.add(t);
			}
		} catch (Exception e) {
			Log.w(TAG, "Exception in toObjectList with cls:" + cls.getSimpleName(), e);
		}
		return list;
	}
}


