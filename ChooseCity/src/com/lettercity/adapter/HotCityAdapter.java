package com.lettercity.adapter;

import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lettercity.choosecity.R;
import com.lettercity.entitiy.City;
import com.lettercity.utils.AdapterUtils;

public class HotCityAdapter extends BaseAdapter {
	private List<City> mList;

	public HotCityAdapter(List<City> list) {
		this.mList = AdapterUtils.getList(list);

	}


	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public City getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(parent.getContext()).inflate(
					R.layout.select_city_item_hot_grid, parent, false);
			viewHolder = new ViewHolder();
			viewHolder.columnTitle = (TextView) convertView
					.findViewById(R.id.column_title);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.columnTitle.setText(mList.get(position).getCityName());

		return convertView;
	}

	static class ViewHolder {
		TextView columnTitle;
	}
}
