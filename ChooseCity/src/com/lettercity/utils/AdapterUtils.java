package com.lettercity.utils;

import java.util.ArrayList;
import java.util.List;

public class AdapterUtils {

	public static <V> List<V> getList(List<V> sourceList) {
		return !ListUtils.isEmpty(sourceList) ? sourceList : new ArrayList<V>();

	}
	public static <V> Object getAdapterItem(List<V> mList, int position) {
		return (mList == null || position >= mList.size()) ? null : mList
				.get(position);
	}

	public static <V> int getAdapterCount(List<V> mList) {
		return mList == null ? 0 : mList.size();
	}
}
