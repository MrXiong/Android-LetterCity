package com.lettercity.choosecity;

import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.lettercity.adapter.CityAdapter;
import com.lettercity.adapter.HotCityAdapter;
import com.lettercity.adapter.SearchCityAdapter;
import com.lettercity.cache.CacheInfoManager;
import com.lettercity.entitiy.City;
import com.lettercity.utils.LetterSortComparator;
import com.lettercity.view.CityLetterSortView;
import com.lettercity.view.ClearEditText;
import com.lettercity.view.NotRollGridView;
import com.lettercity.view.CityLetterSortView.OnTouchingLetterChangedListener;

public class CityActivity extends Activity {
	public final static int REQUEST_CODE = 10;
	public final static String CITY_SELECTED = "citySelected";

	public static final String TAG = CityActivity.class.getSimpleName();
	private ClearEditText mClearEditText;
	private TextView mTvMidLetter;
	private ListView mLvCitylist;
	private CityLetterSortView mRightLetter;
	private CityAdapter mAdapter;
	private List<City> mCitylist;
	private List<City> mHotCitylist;
	private InputMethodManager inputMethodManager;
	private View mCityContainer;
	private FrameLayout mSearchContainer;
	private ListView mSearchListView;
	private SearchCityAdapter mSearchCityAdapter;

	private CacheInfoManager mCacheInfoManager;
	private LayoutInflater mLayoutInflater;
	private View mCityLocalView;
	private View mCityHotView;
	private NotRollGridView mGvSelectCity;
	private TextView mTvCityLocation;
	private HotCityAdapter mHotCityAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_city);
		initView();
	}

	protected void initView() {
		mLayoutInflater = LayoutInflater.from(this);
		inputMethodManager = (InputMethodManager) this
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		mLvCitylist = (ListView) findViewById(R.id.lv_citylist);
		mClearEditText = (ClearEditText) findViewById(R.id.et_msg_search);
		// 这里设置中间字母
		mRightLetter = (CityLetterSortView) findViewById(R.id.right_letter);
		mTvMidLetter = (TextView) findViewById(R.id.tv_mid_letter);
		mRightLetter.setTextView(mTvMidLetter);
		// 搜索
		mCityContainer = findViewById(R.id.city_content_container);
		mSearchContainer = (FrameLayout) this
				.findViewById(R.id.search_content_container);
		mSearchListView = (ListView) findViewById(R.id.search_list);
		mSearchListView.setEmptyView(findViewById(R.id.search_empty));
		mSearchContainer.setVisibility(View.GONE);
		
		initLocalView();
		initHotView();
		initLinstener();
		initData();
	}
	private void initLocalView() {
		mCityLocalView = mLayoutInflater.inflate(R.layout.select_city_item_local, mLvCitylist,false);
		mTvCityLocation = (TextView) mCityLocalView.findViewById(R.id.tv_city_location);
		mLvCitylist.addHeaderView(mCityLocalView);
	}
	private void initHotView() {
		mCityHotView = mLayoutInflater.inflate(R.layout.select_city_item_hot, mLvCitylist,false);
		mGvSelectCity = (NotRollGridView) mCityHotView.findViewById(R.id.gv_select_city);
		mLvCitylist.addHeaderView(mCityHotView);
	}
	protected void initLinstener() {
		mLvCitylist.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				gotoHomeFragment(parent.getAdapter(), position);
			}
		});
		mGvSelectCity.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				gotoHomeFragment(mHotCityAdapter, position);
			}
		});
		mSearchListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				gotoHomeFragment(mSearchCityAdapter, position);
			}
		});
		
		mLvCitylist.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// 隐藏软键盘
				if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
					if (getCurrentFocus() != null)
						inputMethodManager.hideSoftInputFromWindow(
								getCurrentFocus().getWindowToken(),
								InputMethodManager.HIDE_NOT_ALWAYS);
				}
				return false;
			}
		});

		// 设置右侧触摸监听
		mRightLetter.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {

					@Override
					public void onTouchingLetterChanged(String s) {
						int position = mAdapter.getPositionForSection(s.charAt(0));
						if(position != -1) {
							position = position +2;
							mLvCitylist.setSelection(position);
						} else {
							mLvCitylist.setSelection(0);
						}
							

					}
				});

		// 根据输入框输入值的改变来过滤搜索
		mClearEditText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// 当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
				filterData2(s.toString());
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});

	}
	protected void initData() {
		mCacheInfoManager = CacheInfoManager.getInstance(getApplicationContext());
		initCitys();
	}

	private void initCitys() {
		CacheInfoManager cacheInfoManager = mCacheInfoManager;

		mCitylist = cacheInfoManager.getCityList();
		mHotCitylist = cacheInfoManager.getHotCityList();

		LetterSortComparator letterSortComparator = new LetterSortComparator();
		// 根据a-z进行排序源数据
		try {
			Collections.sort(mCitylist, letterSortComparator);
		} catch (Exception e) {
			Log.e(TAG, "citylist is null");
		}

		mAdapter = new CityAdapter(mCitylist);
		mLvCitylist.setEmptyView(findViewById(R.id.citys_list_load));
		mLvCitylist.setAdapter(mAdapter);
		
		mHotCityAdapter = new HotCityAdapter(mHotCitylist);
		mGvSelectCity.setAdapter(mHotCityAdapter);

	}

	protected void gotoHomeFragment(Adapter adapter, int position) {
		City selectedCity = (City) adapter.getItem(position);
		mCacheInfoManager.setChoosedCity(selectedCity);
		Intent intent = new Intent();
		intent.putExtra(CITY_SELECTED, selectedCity);
		setResult(RESULT_OK, intent);
		finish();
	}

	private void filterData2(String filterStr) {
		mSearchCityAdapter = new SearchCityAdapter(mCitylist);
		mSearchListView.setAdapter(mSearchCityAdapter);
		mSearchListView.setTextFilterEnabled(true);
		if (mCitylist.size() < 1 || TextUtils.isEmpty(filterStr)) {
			mCityContainer.setVisibility(View.VISIBLE);
			mSearchContainer.setVisibility(View.INVISIBLE);

		} else {

			mCityContainer.setVisibility(View.INVISIBLE);
			mSearchContainer.setVisibility(View.VISIBLE);
			mSearchCityAdapter.getFilter().filter(filterStr);
		}
	}

}
