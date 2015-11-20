package com.lettercity.utils;

public class ArrayUtils {
	public static <V> boolean isEmpty(V[] sourceArray) {
		return (sourceArray == null || sourceArray.length == 0);
	}
}
