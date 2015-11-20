package com.lettercity.adapter;

import java.util.List;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lettercity.choosecity.R;
import com.lettercity.entitiy.City;
import com.lettercity.utils.AdapterUtils;

public class CityAdapter extends BaseAdapter{
	private static final String TAG = CityAdapter.class.getSimpleName();
	private List<City> mCityList;
	public CityAdapter(List<City> list) {
		mCityList = AdapterUtils.getList(list);
	}
	
	@Override
	public int getCount() {
		return mCityList.size();
	}

	@Override
	public City getItem(int position) {
		
		return mCityList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		City cityBean = mCityList.get(position);
		final String name = cityBean.getCityName();
			ViewHolder viewHolder = null;
			if (convertView == null
					|| convertView.getTag(R.id.tag_defult) == null) {
				convertView = LayoutInflater.from(parent.getContext()).inflate(
						R.layout.select_city_item, parent,false);
				viewHolder = new ViewHolder();
				viewHolder.groupTitle = (TextView) convertView
						.findViewById(R.id.group_title);
				viewHolder.columnTitle = (TextView) convertView
						.findViewById(R.id.column_title);
				convertView.setTag(R.id.tag_defult, viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag(R.id.tag_defult);
			}
			viewHolder.columnTitle.setText(name);
			// 根据position获取分类的首字母的Char ascii值
			int sectio = getSectionForPosition(position);
			// 如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
			if (position == getPositionForSection(sectio)) {
				viewHolder.groupTitle.setVisibility(View.VISIBLE);
				viewHolder.groupTitle.setText(cityBean.getFirstLetter().toUpperCase());
			} else {
				viewHolder.groupTitle.setVisibility(View.GONE);
			}

		return convertView;
	}

	static class ViewHolder {
		TextView groupTitle;// 首字母提示
		TextView columnTitle;
	}


	public int getSectionForPosition(int position) {
		Log.d(TAG, "getSectionForPosition position = " + position);
		City city = mCityList.get(position);
		return city.getFirstLetter().toUpperCase().charAt(0);
	}

	public int getPositionForSection(int section) {
		for (int i = 0; i < getCount(); i++) {
			String sortStr = mCityList.get(i).getFirstLetter();
			char firstChar = sortStr.toUpperCase().charAt(0);
			if (firstChar == section) {
				return i;
			}
		}

		return -1;
	}
}