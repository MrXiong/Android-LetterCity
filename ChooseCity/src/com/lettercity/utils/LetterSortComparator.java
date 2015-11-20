package com.lettercity.utils;

import java.util.Comparator;

import com.lettercity.entitiy.City;

public class LetterSortComparator implements Comparator<City> {

	public int compare(City o1, City o2) {
		if (o1.getFirstLetter().equals("@")
				|| o2.getFirstLetter().equals("#")) {
			return -1;
		} else if (o1.getFirstLetter().equals("#")
				|| o2.getFirstLetter().equals("@")) {
			return 1;
		} else {
			return o1.getFirstLetter().compareTo(o2.getFirstLetter());
		}
	}

}
