
  
package com.lettercity.cache;  

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.lettercity.entitiy.City;
import com.lettercity.gson.GsonTools;

public class CacheInfoManager {

	private static final String TAG = CacheInfoManager.class.getSimpleName();
	private final static String Pref_Name = "com.utils";
	private final static String PREF_KEY = "com.utils.";
	private final static String PREF_KEY_GPS_CITY = PREF_KEY+"gpsCity";
	private final static String PREF_KEY_CHOOSED_CITY = PREF_KEY+"choosedCity";
	private final static String PREF_KEY_HOTCITYLIST = PREF_KEY+"hotCityList";
	private final static String PREF_KEY_CITYLIST = PREF_KEY+"cityList";
	private SharedPreferences mPrefs;
	public static CacheInfoManager mInstance;
	private Context mContext;
	private List<City> mCityList;
	private List<City> mHotCityList;
	private City mGpsCity;
	
	private City mChoosedCity;
	
	private CacheInfoManager(Context context){
		this.mContext = context;
		this.mPrefs = context.getSharedPreferences(Pref_Name, Context.MODE_PRIVATE);
	}
	
	public static CacheInfoManager getInstance(Context context){
		if(null == mInstance) {
			synchronized (CacheInfoManager.class) {
				if(mInstance == null) {
					mInstance = new CacheInfoManager(context);
				}
			}
		}
		
		return mInstance;
	}
	
	private  String readAssertFile(String fileName) {
		InputStream in = null;
		try {
			Log.d(TAG, "readAssertFile: fileName = " + fileName);
			StringBuilder sb = new StringBuilder();
			in = mContext.getAssets().open(fileName);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String line = null;
			while( (line = br.readLine()) != null){
				sb.append(line);
			}
			return sb.toString();
		} catch (Exception e) {
			Log.w(TAG, "Can't read assert file '" + fileName + "'", e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
				}
			}
		}
		return null;
	}
	
	public void setGpsCity(City city) {
		if (null == city) {
			return;
		}
		if (mGpsCity != null && mGpsCity.equals(city)) {
			return;
		}
		this.mGpsCity = city;
		final String strGpsCity = GsonTools.toJson(city);
		mPrefs.edit()
		  .putString(PREF_KEY_GPS_CITY, strGpsCity)
		  .commit();
	}
	public City getGpsCity() {
		if (mGpsCity != null) {
			return mGpsCity;
		}
		final String strGpsCity = mPrefs.getString(PREF_KEY_GPS_CITY, null);
		if (!TextUtils.isEmpty(strGpsCity)) {
			mGpsCity = GsonTools.toObject(strGpsCity, City.class);
			return mGpsCity;
		}

		return mGpsCity;
	}
	
	public void setChoosedCity(City city) {
		if (null == city || city.equals(mChoosedCity)) {
			return;
		}
		this.mChoosedCity = city;
		final String strCity = GsonTools.toJson(city);
		mPrefs.edit()
			  .putString(PREF_KEY_CHOOSED_CITY, strCity)
			  .commit();
				
	}
	
	public City getChoosedCity() {
		if (mChoosedCity != null) {
			return mChoosedCity;
		}
		// 从SharedPreferences数据中初始化
		final String strChoosedCity = mPrefs.getString(PREF_KEY_CHOOSED_CITY, null);
		if (!TextUtils.isEmpty(strChoosedCity)) {
			mChoosedCity = GsonTools.toObject(strChoosedCity, City.class);
			return mChoosedCity;
		}
		if (null == mChoosedCity) {
			City defalutChoosedCity = getHotCityList().get(0);
			setChoosedCity(defalutChoosedCity);
			mChoosedCity = defalutChoosedCity;
		}
		return mChoosedCity;
	}
	
	public List<City> getHotCityList() {
		if (mHotCityList != null) {
			return mHotCityList;
		}
		// 从SharedPreferences数据中初始化
		String strHotCityList = mPrefs.getString(PREF_KEY_HOTCITYLIST, null);
		if (!TextUtils.isEmpty(strHotCityList)) {
			mHotCityList = GsonTools.toObjectList(strHotCityList, City.class);
			return mHotCityList;
		}
		// 从预置的数据中初始化
		strHotCityList = readAssertFile("cache/HotCityList.json");
		if (!TextUtils.isEmpty(strHotCityList)) {
			mHotCityList = GsonTools.toObjectList(strHotCityList, City.class);
			return mHotCityList;
		}
		
		return mHotCityList;
	}
	
	
	public List<City> getCityList() {
		if (mCityList != null) {
			return mCityList;
		}
		// 从SharedPreferences数据中初始化
		String strCityList = mPrefs.getString(PREF_KEY_CITYLIST, null);
		if (!TextUtils.isEmpty(strCityList)) {
			mCityList = GsonTools.toObjectList(strCityList, City.class);
			return mCityList;
		}
		// 从预置的数据中初始化
		strCityList = readAssertFile("cache/CityList.json");
		if (!TextUtils.isEmpty(strCityList)) {
			mCityList = GsonTools.toObjectList(strCityList, City.class);
			return mCityList;
		}
		
		return mCityList;
	}
	
}
  