package com.lettercity.choosecity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.lettercity.entitiy.City;

public class MainActivity extends Activity implements OnClickListener {

	private Button mTvCity;
	private LinearLayout mImagecontainerCity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mTvCity = (Button)findViewById(R.id.tv_city);
		mImagecontainerCity = (LinearLayout)findViewById(R.id.imagecontainer_city);
		
		mTvCity.setOnClickListener(this);
		mImagecontainerCity.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_city:
			startActivityForResult(new Intent(this, CityActivity.class), CityActivity.REQUEST_CODE);
			break;

		default:
			break;
		}
		
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data); 
		if(data == null) {
			return;
		}
		if(requestCode == CityActivity.REQUEST_CODE) {
			City selectedCity = (City) data.getSerializableExtra(CityActivity.CITY_SELECTED);
			if (selectedCity != null) {
				mTvCity.setText(selectedCity.getCityName());
			}
		}
	}

}
