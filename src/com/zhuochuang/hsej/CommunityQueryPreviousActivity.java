package com.zhuochuang.hsej;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.layout.PullToRefreshListView;
import com.zhuochuang.hsej.adapter.CommQueryPriviousAdapter;

/**
 * 往届查询
 * @author Administrator
 *
 */
public class CommunityQueryPreviousActivity extends BaseActivity {

	public static String currentYear;
	private String previousYear;
	private PullToRefreshListView mListView;
	private CommQueryPriviousAdapter mPriviousAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_community_query_previous);

		setTitleText(R.string.title_activity_community_query_previous);
		mListView = (PullToRefreshListView) findViewById(R.id.puulto_listview);
		initView();
	}

	private void initView() {
		currentYear = getIntent().getStringExtra("current_year");
		previousYear = getIntent().getStringExtra("previous_year");
		try {

			JSONArray array = new JSONArray(previousYear);
			final String[] strings = new String[array.length()];
			for (int i = 0; i < strings.length; i++)
				strings[i] = array.getString(i);
			mPriviousAdapter = new CommQueryPriviousAdapter(this, strings);
			mListView.setAdapter(mPriviousAdapter);

			mListView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					currentYear = strings[position - 1];
					mPriviousAdapter.notifyDataSetChanged();
					Intent intent = new Intent();
					intent.putExtra("currentYear", currentYear);
					setResult(Activity.RESULT_OK, intent);
					finish();
				}
			});
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}
}
