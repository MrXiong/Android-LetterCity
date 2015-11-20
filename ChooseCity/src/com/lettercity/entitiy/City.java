package com.lettercity.entitiy;

import java.io.Serializable;

import android.text.TextUtils;
import android.util.Log;

public class City implements Serializable {
	private String cityId;
	private String firstLetter;
	private String cityName;
	
	public City(City city) {
		if (city != null) {
			this.cityId = city.getCityId();
			this.cityName = city.getCityName();
		}
	}
	public City() {
	}
	
	public String getCityId() {
		return cityId;
	}
	public void setCityId(String cityId) {
		this.cityId = cityId;
		this.firstLetter = getFirstLetter(cityId);
	}
	
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	
	public String getFirstLetter() {
		if (!TextUtils.isEmpty(firstLetter)) {
			return firstLetter;
		}
		firstLetter = getFirstLetter(cityId);
		return firstLetter;
	}
	
	private static String getFirstLetter(String str) {
		if (TextUtils.isEmpty(str)) {
			return "";
		}
		return str.substring(0, 1);
	}

	
	public boolean equals(City city) {
		return city != null && this.cityId.equals(city.getCityId());
	}
	

	
}
